package org.matis.park.server.util;

/**
 * Created by manuel on 6/11/14.
 * <p>Base app exception</p>
 */
public class ParkException extends RuntimeException {

    //TODO change all to be based on

    public ParkException(String msg){
        super(msg);
    }

    public ParkException(Throwable th){
        super(th);
    }
}
