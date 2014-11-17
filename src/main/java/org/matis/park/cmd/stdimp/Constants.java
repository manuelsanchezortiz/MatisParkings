package org.matis.park.cmd.stdimp;

/**
 * Created by manuel on 6/11/14.
 * <p>Constants for this package</p>
 */
public class Constants {

    /**
     * Main server ctx on all commands are installed
     */
    public static final String CTX= "/parksvc";

    /**
     * Std command add, see {@link org.matis.park.cmd.stdimp.CmdAdd}
     */
    public static final String CMD_ADD= "add";

    /**
     * Std command update, see {@link org.matis.park.cmd.stdimp.CmdUpdate}
     */
    public static final String CMD_UPDATE = "update";

    /**
     * Std command query, see {@link org.matis.park.cmd.stdimp.CmdQuery}
     */
    public static final String CMD_QUERY = "query";

    /**
     * Std command free slot, see {@link org.matis.park.cmd.stdimp.CmdFreeSlot}
     */
    public static final String CMD_FREE_SLOT = "freeSlot";

    /**
     * Std command use slot, see {@link org.matis.park.cmd.stdimp.CmdUseSlot}
     */
    public static final String CMD_USE_SLOT = "useSlot";
}
