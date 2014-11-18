package org.matis.park.server.util;

/**
 * Created by manuel on 6/11/14.
 * <p>Base app exception</p>
 */
public class ParkException extends RuntimeException {

    public ParkException(String msg){
        super(msg);
    }

    public ParkException(Throwable th){
        super(th);
    }
}
