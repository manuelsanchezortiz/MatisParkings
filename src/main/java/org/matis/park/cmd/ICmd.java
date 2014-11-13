package org.matis.park.cmd;

import com.sun.net.httpserver.HttpHandler;
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

}
