package org.matis.park.cmd;

import java.util.Collection;

/**
 * Created by manuel on 17/11/14.
 * <p>Command container for use with {@link java.util.ServiceLoader}</p>
 */
public interface ICmdGroup {

    /**
     * @return list of commands registered in this module
     */
    public Collection<ICmd> getCommands();
}
