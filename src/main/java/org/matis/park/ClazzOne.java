package org.matis.park;

/**
 * Created by manuel on 5/11/14.
 */
public class ClazzOne {
    public void add(String o) {

        if( o == null || o.length()==0 ){
            throw new RuntimeException("Null or empty args!");
        }

    }
}
