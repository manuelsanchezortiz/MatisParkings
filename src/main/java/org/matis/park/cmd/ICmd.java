package org.matis.park.cmd;

import com.sun.net.httpserver.HttpHandler;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.util.HttpMethod;

/**
 * Created by manuel on 5/11/14.
 */
public interface ICmd extends HttpHandler {

    /**
     *
     * @return http method it will understand
     */
    HttpMethod getHttpMethod();

    /**
     *
     * @return command that implements
     */
    String getCmd();

    /**
     * Parse a response from the raw response for this specific command
     * @param rawResponse, the raw response from the server
     * @return the specific app response for this command. If rawResponse is empty, return null
     */
    CmdResponse parseResponse(String rawResponse);

}
