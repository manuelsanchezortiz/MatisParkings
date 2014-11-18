package org.matis.park.cmd.stdimp;

/**
 * Created by manuel on 6/11/14.
 * <p>Command error codes and related message</p>
 * <p>Note: Not using enums to fix codes and allow new ones</p>
 */
public class CmdErrorCodes {

    /**
     * No error
     */
    public static final int NONE = 0;

    /**
     * Invalid response codes
     */
    public static int INVALID_RESPONSE= -100;

    /**
     * Duplicated entity
     */
    public static final int DUPLICATED = -200;

    /**
     * Entity is invalid
     */
    public static final int INVALID_ENTITY= -300;

    /**
     * Field is null or invalid
     */
    public static final int NULL_OR_INVALID_FIELD = -400;

    /**
     * Entity not found, ex. when updating
     */
    public static final int ENTITY_NOT_FOUND= -500;

    /**
     * No entity, the command needs and entity which was not found
     */
    public static final int NO_ENTITY = -600;

    /**
     * No version header (http) was found
     */
    public static final int NO_VERSION = -700;

    /**
     * Protocol error: Evaluated by the server when receives commands
     */
    public static final int PROTOCOL_ERROR = -1000;

    //TODO related messages, from a .properties perhaps localized
    public static String getMessage(int appCode, Object... args){
        return new Integer(appCode).toString();
    }
}
