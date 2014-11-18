package org.matis.park.server;

/**
 * Created by manuel on 6/11/14.
 * <p>Keys for objects in the server httpContext so they are accessible in each request</p>
 */
public class ServerCtx {

    /**
     * Contains Parking Dao: for a more complex application would be a persistent manager
    */
    public static final String PARKING_PERSISTENT_DAO= "parkingPersistentDao";

    /**
     * Put the registry con the context to easy access to it
     */
    public static final String COMMAND_REGISTRY= "commandRegistry";
}
