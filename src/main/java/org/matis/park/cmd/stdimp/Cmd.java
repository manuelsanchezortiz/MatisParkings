package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.ICmdHttpHandler;
import org.matis.park.dto.CmdResponseSerializer;
import org.matis.park.server.util.HttpMethod;
import org.matis.park.server.util.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static org.matis.park.Utils.checkNotEmpty;
import static org.matis.park.server.Logger.LOGGER;

/**
 * Created by manuel on 6/11/14.
 * <p>Basic command functionality</p>
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
     * <p>Checks http method and return:</p>
     * <ul>
     *     <li>Method is invalid or in subclasses malformed in any way: {@link org.matis.park.server.util.HttpStatus#BAD_REQUEST} </li>
     *     <li>Else  Ok</li>
     * </ul>
     * <p>Then calls {@link #handleRequest(com.sun.net.httpserver.HttpExchange)} this is the method you must override</p>
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if( !httpExchange.getRequestMethod().equals( this.getHttpMethod().name() ) ){

            //Protocol Error
            this.sendResponse(httpExchange, HttpStatus.BAD_REQUEST, CmdResponse.CMD_RESPONSE_PROTOCOL_ERROR);
            return;
        }

        try {
            this.handleRequest(httpExchange);
        }catch( RuntimeException re){
            LOGGER.log(Level.WARNING, "Something wrong", re);
            //send internal server error and rethrow
            this.sendInternalError(httpExchange);

            throw re;
        }catch( IOException e){
            LOGGER.log(Level.WARNING, "Something wrong", e);

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
     * Send response
     * @param httpExchange, the exchange data
     * @param httpStatus, status to send
     * @param cr, the response to encode
     */
    protected void sendResponse( HttpExchange httpExchange, int httpStatus, CmdResponse cr ) throws IOException{
        Headers responseHeaders= httpExchange.getResponseHeaders();

        //java is utf-8
        responseHeaders.set("Content-Type", "text/html;charset=UTF-8");

        CmdResponseSerializer responseSerializer= new CmdResponseSerializer();

        StringWriter sw= new StringWriter();
        responseSerializer.encode( cr, new BufferedWriter(sw) );
        String response= sw.toString();

        byte[] bytes= response.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(httpStatus, bytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
