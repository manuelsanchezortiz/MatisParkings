package org.matis.park.dto;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by manuel on 6/11/14.
 * <p>Is transferable if implements encode/decode; code and version: format major/minor
 * version. Major version are incompatible</p>
 */
public interface StringSerializer<T> {

    /**
     * Identifies the transferable policy
     * @return
     */
    public String getCode();

    /**
     * Major version, compatible
     * @return
     */
    public int getMajorVersion();

    /**
     * Minor version, incompatible
     * @return
     */
    public int getMinorVersion();

    /**
     * Encode the object
     * @param o, object to encode
     * @param w, where to encode (could have been a string builder)
     */
    public void encode(T o, BufferedWriter w);

    /**
     * Decode the object
     * @param r,encoded object as a string, we use a buffered reader to read line by line
     * @return the new object, maybe is null
     */
    public T decode(BufferedReader r);


}
