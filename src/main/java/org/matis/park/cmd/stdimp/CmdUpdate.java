package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.HttpExchange;
import org.matis.park.Constants;
import org.matis.park.ServerCtx;
import org.matis.park.dao.ParkingDao;
import org.matis.park.dto.ParkingSerializer;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.HttpMethod;
import org.matis.park.util.HttpStatus;
import org.matis.park.util.ParkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static org.matis.park.Logger.LOGGER;

/**
 * Created by manuel on 6/11/14.
 * <p>Update parking: Updates existing object, only if exists. The id can not be changed as it used
 * to identify the object to replace</p>
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
public class CmdUpdate extends Cmd {

    public CmdUpdate(){
        super(Constants.CMD_UPDATE);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    /**
     * <p>Updates existing parking. Get a parking object from the post data and calls {@link org.matis.park.dao.ParkingDao#update(org.matis.park.modelobj.Parking)}</p>
     * @param httpExchange
     * @throws IOException
     */
    protected void handleRequest(HttpExchange httpExchange) throws IOException {
        //TODO select it from factory using protocol version (attribute on request)

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Cmd update");
        }

        //load the post data
        ParkingSerializer dto= new ParkingSerializer();

        //TODO improve read n parkings, the null parking may exists or not !!!
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

        CmdResponse cr= parkingDao.update(parking);

        this.sendResponse(httpExchange, HttpStatus.OK, cr);
    }
}
