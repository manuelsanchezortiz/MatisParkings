package org.matis.park.cmd;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.matis.park.cmd.stdimp.CmdErrorCodes;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.server.ServerCtx;
import org.matis.park.server.util.HttpStatus;

import java.io.IOException;
import java.util.List;

/**
 * Created by manuel on 17/11/14.
 * <p>This class is registered for receiving the request and route it to the desired version of the command. So
 * it needs a CMD_VERSION header field on the request else we get a protocol error</p>
 * <p>With the version, it routes the handle request method to the desired command</p>
 */
public class CmdProxy implements HttpHandler {

    private String cmd; //command name

    public CmdProxy( String cmd){
        this.cmd= cmd;
    }

    private static final String HEADER_CMD_VERSION= "CMD_VERSION";


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        List<String> headers= httpExchange.getRequestHeaders().get(HEADER_CMD_VERSION);

        if( headers != null && headers.size() > 0  ){

            CmdRegistry reg= (CmdRegistry)httpExchange.getHttpContext().getAttributes().get(ServerCtx.COMMAND_REGISTRY);

            if( reg == null ){
                //Protocol Error
                Tools.sendResponse(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR, CmdResponse.CMD_INTERNAL_SERVER_ERROR);
                return;
            }

            String version= headers.get(0);

            ICmd cmd= reg.getCmd(this.cmd, version);

            if( cmd == null ){
                Tools.sendResponse(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR, CmdResponse.CMD_INTERNAL_SERVER_ERROR);
                return;
            }

            //execute the real command
            cmd.handle(httpExchange);
        }else{
            //Protocol error: NO Version header
            Tools.sendResponse(httpExchange, HttpStatus.BAD_REQUEST, new CmdResponse( CmdErrorCodes.NO_VERSION, CmdErrorCodes.getMessage(CmdErrorCodes.NO_VERSION) ));

        }

    }
}
