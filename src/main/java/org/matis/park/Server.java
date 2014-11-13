package org.matis.park;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.matis.park.cmd.CmdRegistry;
import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.ICmdRegistry;
import org.matis.park.cmd.stdimp.*;
import org.matis.park.dao.ParkingDao;

import java.net.InetSocketAddress;
import java.util.logging.Level;

import static org.matis.park.Logger.LOGGER;

/**
 * Created by manuel on 6/11/14.
 */
public class Server {

    private HttpServer server = null;
    private ICmdRegistry cmdRegistry = new CmdRegistry();

    /**
     * Access to data
     * @return
     */
    public ParkingDao getParkingDao() {
        return parkingDao;
    }

    private ParkingDao parkingDao= new ParkingDao();

    /**
     * Access the registry
     * @return command registry
     */
    public ICmdRegistry getCmdRegistry(){
        return this.cmdRegistry;
    }

    /**
     * Start the server
     * @throws Exception
     */
    public void start() throws Exception {

        if( this.server != null ){
            //already running
            return;
        }

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Starting server...");
        }

        registerCmds(cmdRegistry);

        this.server = HttpServer.create(new InetSocketAddress(8081), 0);

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


    private static void registerCmds(ICmdRegistry cmdRegistry) {
        cmdRegistry.addCmd(new CmdAdd());
        cmdRegistry.addCmd(new CmdUpdate());
        cmdRegistry.addCmd(new CmdQuery());
        cmdRegistry.addCmd(new CmdFreeSlot());
        cmdRegistry.addCmd(new CmdUseSlot());
    }

}