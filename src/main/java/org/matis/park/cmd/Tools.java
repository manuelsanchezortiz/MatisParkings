package org.matis.park.cmd;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.dto.CmdResponseSerializer;
import org.matis.park.server.util.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by manuel on 17/11/14.
 * <p>Some tools</p>
 */
public class Tools {

    /**
     * Fast send ok
     * @param httpExchange
     * @throws java.io.IOException
     */
    public static void sendOk(HttpExchange httpExchange) throws IOException {

        sendResponse(httpExchange, HttpStatus.OK, CmdResponse.CMD_RESPONSE_OK);

    }

    /**
     * Fast send internal error, some error caused by an unexpected exception
     * @param httpExchange
     * @throws IOException
     */
    public static void sendInternalError(HttpExchange httpExchange) throws IOException {

        sendResponse(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR, CmdResponse.CMD_INTERNAL_SERVER_ERROR);

    }

    /**
     * Send response
     * @param httpExchange, the exchange data
     * @param httpStatus, status to send
     * @param cr, the response to encode
     */
    public static void sendResponse( HttpExchange httpExchange, int httpStatus, CmdResponse cr ) throws IOException{
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
