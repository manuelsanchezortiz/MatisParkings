package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.Constants;
import org.matis.park.ServerCtx;
import org.matis.park.dao.ParkingDao;
import org.matis.park.util.HttpMethod;
import org.matis.park.util.ParkException;
import org.matis.park.util.TestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 * <p>Queries Parking</p>
 * <ul>
 *     <li>Http method: GET</li>
 *     <li>Arguments in url:</li>
 *     <li>Returns, error: Http BAD_REQUEST if params are wrong</li>
 *     <li>Returns: Http OK, {@link org.matis.park.cmd.stdimp.CmdQueryResponse} after action execution</li>
 *     <li>Returned CmdResponse code: {@link org.matis.park.cmd.stdimp.CmdErrorCodes#NONE}: Operation succeeded</li>
 * </ul>
 * ????
 * TODO explain better
 *
 */
public class CmdQuery extends Cmd {

    public CmdQuery(){
        super(Constants.CMD_QUERY);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        ParkingDao parkingDao= (ParkingDao)httpExchange.getHttpContext().getAttributes().get(ServerCtx.PARKING_PERSISTENT_DAO);

        if ( parkingDao == null ){
            //this is a major failure
            throw new ParkException( "Server context has no parking dao");
        }

        Map<String,String> params= TestUtils.decodeParamsFromQueryString(httpExchange.getRequestURI().getQuery() );




    }

    /**
     * Encode the response
     * @return the response with format appcode:appmessage
     */
    public String buildRawResponse( CmdQueryResponse cr ){

        StringBuilder sb= new StringBuilder();
        sb.append(cr.getAppCode());
        sb.append(":");
        sb.append(new Integer(cr.getCount()).toString());
        sb.append(":");
        sb.append(cr.isEnd() ? "Y" : "N");
        sb.append(":");
        sb.append(cr.getAppMessage());

        sb.append(Constants.LINE_SEP);

        //append the objects

        return sb.toString();
    }


    protected void sendResponse( HttpExchange httpExchange, int httpStatus, CmdQueryResponse cr ) throws IOException {
        Headers responseHeaders= httpExchange.getResponseHeaders();

        //java is utf-8
        responseHeaders.set("Content-Type", "text/html;charset=UTF-8");

        //TODO build response change it to CR auto encode

        String response= this.buildRawResponse(cr);
        byte[] bytes= response.getBytes(StandardCharsets.UTF_8.name());
        httpExchange.sendResponseHeaders(httpStatus, bytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
