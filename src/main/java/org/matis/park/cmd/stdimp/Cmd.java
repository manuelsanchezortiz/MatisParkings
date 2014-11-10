package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.ICmdHttpHandler;
import org.matis.park.util.HttpMethod;
import org.matis.park.util.HttpStatus;
import org.matis.park.util.TestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.matis.park.util.TestUtils.checkNotEmpty;

/**
 * Created by manuel on 6/11/14.
 */
public abstract class Cmd implements ICmd {

    private String id;
    private String context;
    private HttpMethod httpMethod;
    private String cmd;
    private ICmdHttpHandler handler;

    public Cmd( String cmd ){

        checkNotEmpty(cmd, "cmd is missing");

        this.cmd= cmd;
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    /**
     * Raw response to parse from clients (integrated like unit testing)
     * @param rawResponse, the raw response from the server
     * @return a command response. If data is corrupted, we return an error
     */
    @Override
    public CmdResponse parseResponse(String rawResponse) {
        if(TestUtils.isEmpty(rawResponse)){
            return null;
        }

        String s[]= rawResponse.split(":");

        //must be two built with buildRawResponse, else very weird error,
        //the same for the int

        if( s.length != 2){
            return CmdResponse.CMD_RESPONSE_INVALID;
        }

        int appCode;
        try {
            appCode = Integer.parseInt(s[0]);
        }catch (NumberFormatException e){
            return CmdResponse.CMD_RESPONSE_INVALID;
        }

        CmdResponse cr= new CmdResponse( appCode, s[1]);

        return cr;
    }

    /**
     * Builds raw response to use on the server
     * @param cr
     * @return
     */
    public String buildRawResponse( CmdResponse cr ){
        StringBuilder sb= new StringBuilder();
        sb.append(cr.getAppCode());
        sb.append(":");
        sb.append(cr.getAppMessage());

        return sb.toString();
    }

    /**
     * <p>Checks http method and return:</p>
     * <ul>
     *     <li>Method is invalid or in subclasses malformed in any way: {@link org.matis.park.util.HttpStatus#BAD_REQUEST} </li>
     *     <li>Else  Ok</li>
     * </ul>
     * <p>Then calls {@link #handleRequest(com.sun.net.httpserver.HttpExchange)} this is the method you must override</p>
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if( httpExchange.getRequestMethod().equals( this.getHttpMethod() ) ){

            //Protocol Error
            this.sendResponse(httpExchange, HttpStatus.BAD_REQUEST, CmdResponse.CMD_RESPONSE_PROTOCOL_ERROR);
            return;
        }

        try {
            this.handleRequest(httpExchange);
        }catch( RuntimeException re){
            //TODO logging
            //send internal server error and rethrow
            this.sendInternalError(httpExchange);

            throw re;
        }catch( IOException e){
            //send internal server error and rethrow
            //TODO logging
            this.sendInternalError(httpExchange);
            throw e;
        }

    }

    /**
     * Returns ok, subclasses must do they job
     * @param httpExchange
     * @throws IOException
     */
    protected void handleRequest(HttpExchange httpExchange) throws IOException {

        this.sendOk(httpExchange);
    }

    /**
     * Fast send ok
     * @param httpExchange
     * @throws IOException
     */
    protected void sendOk(HttpExchange httpExchange) throws IOException {

        this.sendResponse(httpExchange, HttpStatus.OK, CmdResponse.CMD_RESPONSE_OK);

    }

    /**
     * Fast send internal error, some error caused by an unexpected exception
     * @param httpExchange
     * @throws IOException
     */
    protected void sendInternalError(HttpExchange httpExchange) throws IOException {

        this.sendResponse(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR, CmdResponse.CMD_INTERNAL_SERVER_ERROR);

    }

    /**
     * Send response, used by subclasses
     * @param httpStatus
     * @param cr
     */
    protected void sendResponse( HttpExchange httpExchange, int httpStatus, CmdResponse cr ) throws IOException{
        Headers responseHeaders= httpExchange.getResponseHeaders();

        //java is utf-8
        responseHeaders.set("Content-Type", "text/html;charset=UTF-8");

        //TODO buildresponse change it to CR auto encode

        String response= this.buildRawResponse(cr);
        byte[] bytes= response.getBytes(StandardCharsets.UTF_8.name());
        httpExchange.sendResponseHeaders(httpStatus, bytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
