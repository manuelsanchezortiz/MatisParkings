package org.matis.park.cmd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.matis.park.server.Logger.LOGGER;
import static org.matis.park.Utils.checkNotEmpty;
import static org.matis.park.Utils.checkNotNull;

/**
 * Created by manuel on 5/11/14.
 *
 *
 * <p>Implementation notes</p>:
 * <ul>
 *     <li>Unchecked exceptions used as this class is developer oriented and new commands will be usually correct</li>
 *     <li>No message translations of errors, as this class is developer oriented</li>
 * </ul>
 *
 */
public class CmdRegistry {

    private Map<String, ICmd> commands= new HashMap<String, ICmd>(5);


    /**
     * Thrown when an already registered cmd has the same id. The message is the cmd id we are
     * trying to register again
     */
    public static class AlreadyRegisteredException extends RuntimeException {

        public AlreadyRegisteredException(String cmdId){
            super(cmdId);
        }
    }

    /**
     * Thrown when is an invalid command
     */
    public static class InvalidCmd extends RuntimeException {

    }

    /**
     * Adds commands to the registry
     * @param cmd, command to add
     */
    public void addCmd(ICmd cmd){

        this.checkIsAGoodCommand(cmd);

        if( this.commands.containsKey(cmd.getCmd())){
            throw new AlreadyRegisteredException(cmd.getCmd());
        }

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Adding cmd {0}", cmd.getCmd());
        }

        this.commands.put(cmd.getCmd(), cmd);
    }

    /**
     * @return the collection of commands registered
     */
    public Collection<ICmd> getCmds() {
        return this.commands.values();
    }

    /**
     * Get the desired command
     * @param id, of the command
     * @return null if id is not found or the cmd for this id
     */
    public ICmd getCmd(String id) {
        return this.commands.get(id);
    }

    /**
     * Checks is a good cmd
     * <ul>
     *     <li>id is mandatory</li>
     *     <li>id is mandatory</li>
     * </ul>
     * @param cmd, the command to check
     */
    protected void checkIsAGoodCommand(ICmd cmd){

        checkNotNull(cmd.getHttpMethod(), "http method is missing");
        checkNotEmpty(cmd.getCmd(), "cmd is missing");
    }
}