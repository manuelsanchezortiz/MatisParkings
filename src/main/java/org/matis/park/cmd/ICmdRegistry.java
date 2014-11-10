package org.matis.park.cmd;

import java.util.Collection;

/**
 * Created by manuel on 5/11/14.
 */
public interface ICmdRegistry {

    /**
     * Adds commands to the registry
     * @param cmd
     */
    public void addCmd(ICmd cmd);

    /**
     *
     * @return all commands
     */
    public Collection<ICmd> getCmds();

    /**
     *
     * @param id, of the command
     * @return the command with the id of null
     */
    public ICmd getCmd(String id);
}
