package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.server.ServerCtx;
import org.matis.park.dao.ParkingDao;
import org.matis.park.dto.CmdQueryResponseSerializer;
import org.matis.park.model.Parking;
import org.matis.park.server.util.HttpMethod;
import org.matis.park.server.util.HttpStatus;
import org.matis.park.server.util.ParkException;
import org.matis.park.Utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;

import static org.matis.park.cmd.stdimp.SharedConstants.*;
import static org.matis.park.server.Logger.LOGGER;

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

    /**
     * <p>Handle the query, params</p>
     * <ul>
     *     <li>offset, type int, where to start results, defaults to 0</li>
     *     <li>count, type int, how many results to retrieve, defaults to 10</li>
     *     <li>open, type boolean, T (case insensitive) else false, if present filters by parkings open status now</li>
     *     <li>full, type boolean, T (case insensitive) else false, if present filters by parkings with or without available slots</li>
     *     <li>lat, long, type float, format 0.000000, filters to those near them. {@link org.matis.park.model.Parking#NEAR_METERS} in meters</li>
     *     <li></li>
     * </ul>
     * @param httpExchange, the exchange data
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Cmd query");
        }


        final ParkingDao parkingDao= (ParkingDao)httpExchange.getHttpContext().getAttributes().get(ServerCtx.PARKING_PERSISTENT_DAO);

        if ( parkingDao == null ){
            //this is a major failure
            throw new ParkException( "Server context has no parking dao");
        }

        final Map<String,String> params= Utils.decodeParamsFromQueryString(httpExchange.getRequestURI().getQuery());

        //return a list of objects, first test
        int offset= 0;
        int count= QUERY_PAGE_SIZE;
        Boolean open= null;
        Boolean full= null;
        Float lat= null;
        Float longitude= null;

        if( params != null ) {
            offset = params.containsKey(PARAM_OFFSET) ? Integer.parseInt(params.get(PARAM_OFFSET)) : 0;

            count = params.containsKey(PARAM_COUNT) ? Integer.parseInt(params.get(PARAM_COUNT)) : QUERY_PAGE_SIZE;

            open = params.containsKey(PARAM_OPEN) ? (params.get(PARAM_OPEN).trim().toLowerCase().equals(PARAM_TRUE)) : null;
            full = params.containsKey(PARAM_FULL) ? (params.get(PARAM_FULL).trim().toLowerCase().equals(PARAM_TRUE)) : null;


            if (params.containsKey(PARAM_LAT)) {
                Number n;
                try {
                    n = Utils.DECIMAL_FORMAT.parse(params.get(PARAM_LAT));
                } catch (ParseException e) {
                    throw new ParkException("Malformed param '" + PARAM_LAT + "'");
                }
                if (n != null) {
                    lat = n.floatValue();
                }
            }


            if (params.containsKey(PARAM_LONG)) {
                Number n;
                try {
                    n = Utils.DECIMAL_FORMAT.parse(params.get(PARAM_LONG));
                } catch (ParseException e) {
                    throw new ParkException("Malformed param '" + PARAM_LONG + "'");
                }
                if (n != null) {
                    longitude = n.floatValue();
                }
            }
        }
        final Boolean finalOpen= open;
        final Boolean finalFull= full;
        final Float finalLong= longitude;
        final Float finalLat= lat;

        ParkingDao.Filter filter= null;

        //assign a filter only if its needed
        if( finalOpen != null || finalFull != null || (finalLat != null && finalLong != null )){
            new ParkingDao.Filter(){
                public boolean accept(Parking p){

                    boolean r= true;

                    if( finalOpen != null ){
                        GregorianCalendar gc= new GregorianCalendar();

                        boolean isOpened= p.isOpened(gc);

                        r= r && ( finalOpen ? isOpened : !isOpened);
                    }

                    if( finalFull != null ){

                        boolean isFull= p.isFull();

                        r= r && ( finalFull ? isFull : !isFull );
                    }

                    if( finalLat != null && finalLong != null ){

                        r= r && p.isNear( finalLat, finalLong);
                    }

                    return r;
                }
            };
        }

        ParkingDao.QueryResult qr= parkingDao.queryAll( offset, count, filter);

        CmdQueryResponse cmdQueryResponse= new CmdQueryResponse(
                CmdErrorCodes.NONE,
                qr.getParkings().size(),
                qr.isEnd(),
                CmdErrorCodes.getMessage(CmdErrorCodes.NONE),
                qr.getParkings());

        //return the response
        this.sendResponse( httpExchange, HttpStatus.OK ,cmdQueryResponse);
    }

    /**
     * Send response
     * @param httpExchange, the exchange data
     * @param httpStatus, status to send
     * @param cr, response to encode
     */
    protected void sendResponse( HttpExchange httpExchange, int httpStatus, CmdQueryResponse cr ) throws IOException{
        Headers responseHeaders= httpExchange.getResponseHeaders();

        //java is utf-8
        responseHeaders.set("Content-Type", "text/html;charset=UTF-8");

        CmdQueryResponseSerializer responseSerializer= new CmdQueryResponseSerializer();

        StringWriter sw= new StringWriter();
        BufferedWriter bw= new BufferedWriter( sw );
        responseSerializer.encode( cr, bw );
        bw.close();
        String response= sw.toString();

        byte[] bytes= response.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(httpStatus, bytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
