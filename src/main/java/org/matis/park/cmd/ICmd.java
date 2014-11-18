package org.matis.park.cmd;

import com.sun.net.httpserver.HttpHandler;
import org.matis.park.server.util.HttpMethod;

/**
 * Created by manuel on 5/11/14.
 * <p>Interface for implementing new commands</p>
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
     * Version allows multiple versions of the same command to coexist
     * @return
     */
    String getVersion();

}
