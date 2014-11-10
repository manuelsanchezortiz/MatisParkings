package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.HttpExchange;
import org.matis.park.Constants;
import org.matis.park.ServerCtx;
import org.matis.park.dao.ParkingDao;
import org.matis.park.util.HttpMethod;
import org.matis.park.util.HttpStatus;
import org.matis.park.util.ParkException;
import org.matis.park.util.TestUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 * <p>Free slot: Frees an slot, so available slots get increased. This value remains valid between 0 and total
 * slots. As it's optional nothings happens if it's null in the parking object</p>
 *
 * <ul>
 *     <li>Http method: POST</li>
 *     <li>Arguments in POST payload: query string url encoded (as usual param=value)</li>
 *     <li>Param: {@link #PARAM_ID}, id of the parking to use, value type: int</li>
 *     <li>Returns, error: Http BAD_REQUEST if param is missing</li>
 *     <li>Returns: Http OK, {@link org.matis.park.cmd.stdimp.CmdResponse} after action execution</li>
 *     <li>Returned CmdResponse code: {@link org.matis.park.cmd.stdimp.CmdErrorCodes#NONE}: Operation succeeded</li>
 * </ul>
 */
public class CmdFreeSlot extends Cmd {

    public static final String PARAM_ID= "id";

    public CmdFreeSlot(){
        super(Constants.CMD_FREE_SLOT);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

    /**
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        ParkingDao parkingDao= (ParkingDao)httpExchange.getHttpContext().getAttributes().get(ServerCtx.PARKING_PERSISTENT_DAO);

        if ( parkingDao == null ){
            //this is a major failure
            throw new ParkException( "Server context has no parking dao");
        }

        Map<String,String> params= TestUtils.decodeParamsFromQueryString( httpExchange.getRequestBody() );


        Integer id=  null;

        if( params.containsKey( PARAM_ID) && !TestUtils.isEmpty(params.get(PARAM_ID))) {
            try {
                id = Integer.parseInt(params.get(PARAM_ID));
            } catch (NumberFormatException ex) {
                //ignore
            }
        }

        if( id == null ){
            //protocol error
            this.sendResponse(httpExchange, HttpStatus.BAD_REQUEST, CmdResponse.CMD_RESPONSE_PROTOCOL_ERROR );
        }


        CmdResponse cr= parkingDao.freeSlot(id);

        this.sendResponse(httpExchange, HttpStatus.OK, cr);
    }
}
