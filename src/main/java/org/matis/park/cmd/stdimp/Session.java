package org.matis.park.cmd.stdimp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manuel on 18/11/14.
 * <p>Contains per session data and it is set on every service thread</p>
 */
public class Session extends ThreadLocal<Map<String,Object>> {

    public Map<String,Object> initialValue(){

        return new HashMap<String, Object>();
    }

    /**
     * Store the current locale
     */
    public static final String LOCALE= "locale";

    /**
     * Per thread session
     */
    public static Session session= new Session();
}
