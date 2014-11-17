package org.matis.park.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.matis.park.cmd.CmdRegistry;
import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.ICmdGroup;
import org.matis.park.cmd.stdimp.*;
import org.matis.park.dao.ParkingDao;

import java.net.InetSocketAddress;
import java.util.ServiceLoader;
import java.util.logging.Level;

import static org.matis.park.server.Logger.LOGGER;

/**
 * Created by manuel on 6/11/14.
 * <p>Wraps a {@link com.sun.net.httpserver.HttpServer} configuring it to serve the implemented commands</p>
 */
public class Server {

    public static final int DEFAULT_PORT= 8080;

    private HttpServer server = null;
    private CmdRegistry cmdRegistry = new CmdRegistry();

    /**
     * Access to data
     * @return the data access object to access parking data objects
     */
    public ParkingDao getParkingDao() {
        return parkingDao;
    }

    private ParkingDao parkingDao= new ParkingDao();

    /**
     * Access the registry
     * @return command registry
     */
    public CmdRegistry getCmdRegistry(){
        return this.cmdRegistry;
    }

    /**
     * Start the server on default port which is {@link #DEFAULT_PORT}
     * @throws Exception
     */
    public void start() throws Exception {

        this.start(DEFAULT_PORT);
    }

    /**
     * Start the server using a specific port
     * @param port, listening port
     * @throws Exception
     */
    public void start(int port) throws Exception {

        if( this.server != null ){
            //already running
            return;
        }

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Starting server...");
        }

        registerCmds(cmdRegistry);

        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        String ctx = Constants.CTX + "/";

        for (ICmd cmd : cmdRegistry.getCmds()) {

            HttpContext httpCtx= server.createContext(ctx + cmd.getCmd(), cmd);
            httpCtx.getAttributes().put( ServerCtx.PARKING_PERSISTENT_DAO, parkingDao );
        }

        server.setExecutor(null); // creates a default executor

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Started server!");
        }
        server.start();
    }

    /**
     * Stop the server
     */
    public void stop() {
        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Stopping server...");
        }

        this.server.stop(0);
        this.server= null;

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Stopped server");
        }

    }


    private static void registerCmds(CmdRegistry cmdRegistry) {

        ServiceLoader<ICmdGroup> groups = ServiceLoader.load(ICmdGroup.class);

        for( ICmdGroup cmdGroup: groups ){

            for( ICmd cmd: cmdGroup.getCommands() ){
                cmdRegistry.addCmd( cmd );
            }

        }
    }

}