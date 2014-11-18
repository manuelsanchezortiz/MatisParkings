package org.matis.park.cmd.stdimp;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by manuel on 6/11/14.
 * <p>Command error codes and related message</p>
 * <p>Note: Not using enums to fix codes and allow new ones</p>
 */
public class CmdErrorCodes {

    /**
     * No error. Message args: None
     */
    public static final int NONE = 0;

    /**
     * Invalid response codes. Message args: None
     */
    public static int INVALID_RESPONSE= -100;

    /**
     * Duplicated entity. Message args: None
     */
    public static final int DUPLICATED = -200;

    /**
     * Field is null or invalid. Message args: None
     */
    public static final int NULL_OR_INVALID_FIELD = -400;

    /**
     * Entity not found, ex. when updating. Message args: None
     */
    public static final int ENTITY_NOT_FOUND= -500;

    /**
     * No entity, the command needs and entity which was not found. Message args: None
     */
    public static final int NO_ENTITY = -600;

    /**
     * No version header (http) was found. Message args: None
     */
    public static final int NO_VERSION = -700;

    /**
     * Protocol error: Evaluated by the server when receives commands. Message args: None
     */
    public static final int PROTOCOL_ERROR = -1000;

    /**
     * The message bundle contains a format string fo each appcode,
     */
    private static final String MESSAGES_BUNDLE_NAME= "org.matis.park.cmd.stdimp.Messages";

    /**
     * Messages for each appCode and formatted with args
     * @param appCode, application code
     * @param args, arguments if the message have them. Check each error for details of arguments
     * @return the message
     */
    public static String getMessage(int appCode, Object... args){

        Locale locale= Locale.getDefault();

        if( Session.session.get() != null) {
            locale= (Locale) Session.session.get().get(Session.LOCALE);
        }

        if( locale == null ){
            locale= Locale.getDefault();
        }

        ResourceBundle bundle= ResourceBundle.getBundle(MESSAGES_BUNDLE_NAME, locale);

        String s= bundle.getString(String.valueOf(appCode));

        return MessageFormat.format(s, args);
    }
}
