package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.HttpExchange;
import org.matis.park.server.ServerCtx;
import org.matis.park.dao.ParkingDao;
import org.matis.park.dto.ParkingSerializer;
import org.matis.park.model.Parking;
import org.matis.park.server.util.HttpMethod;
import org.matis.park.server.util.HttpStatus;
import org.matis.park.server.util.ParkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static org.matis.park.server.Logger.LOGGER;

/**
 * Created by manuel on 6/11/14.
 * Created by manuel on 6/11/14.
 * <p>Add a parking: Adds a parking object, only if not already there. </p>
 *
 * <ul>
 *     <li>Http method: POST</li>
 *     <li>Header content-type used to identify the object encoding; coder and version</li>
 *     <li>Arguments in POST payload: Encoded parking object. One at a time in this implementation</li>
 *     <li>Returns, error: Http BAD_REQUEST if param is missing</li>
 *     <li>Returns: Http OK, {@link org.matis.park.cmd.stdimp.CmdResponse} after action execution</li>
 *     <li>Returned CmdResponse code: {@link org.matis.park.cmd.stdimp.CmdErrorCodes#NONE}: Operation succeeded</li>
 * </ul>
 */
public class CmdAdd extends Cmd {


    public CmdAdd(){
        super(Constants.CMD_ADD);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    /**
     * * <p>Adds a parking. Get a parking object from the post data and calls {@link org.matis.park.dao.ParkingDao#insert(org.matis.park.model.Parking)}</p>
     * @param httpExchange, the exchange data
     * @throws IOException
     */
    protected void handleRequest(HttpExchange httpExchange) throws IOException {

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Cmd Add parking");
        }

        //load the post data
        ParkingSerializer dto= new ParkingSerializer();

        BufferedReader br= new BufferedReader( new InputStreamReader( httpExchange.getRequestBody(), StandardCharsets.UTF_8) );
        Parking parking= dto.decode(br);

        if( parking == null ){

            //invalid request
            this.sendResponse(httpExchange, HttpStatus.OK, new CmdResponse( CmdErrorCodes.NO_ENTITY, CmdErrorCodes.getMessage(CmdErrorCodes.NO_ENTITY)));
            return;
        }

        ParkingDao parkingDao= (ParkingDao)httpExchange.getHttpContext().getAttributes().get(ServerCtx.PARKING_PERSISTENT_DAO);

        if ( parkingDao == null ){
            //this is a major failure, superclass will return a standard error
            throw new ParkException( "Server context has no parking dao");
        }

        CmdResponse cr= parkingDao.insert(parking);

        this.sendResponse(httpExchange, HttpStatus.OK, cr);
    }
}
