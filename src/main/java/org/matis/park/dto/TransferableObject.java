package org.matis.park.dto;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by manuel on 6/11/14.
 * <p>Is transferable if implements encode/decode; code and version: format major/minor
 * version. Major version are incompatible</p>
 */
public interface TransferableObject<T> {

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
     * @param os, where o is encoded
     */
    public String encode(T o);

    /**
     * Decode the object
     * @param T, desired return type
     * @param is, data input
     * @return the new object, maybe is null
     */
    public T decode(Class<? extends T> T, String s);

    /**
     * Must return the content type we are creating
     * @return the content type
     */
    public String getContentType();

    /**
     * Encoding, in case content type is text
     * @return the encoding type
     */
    public String getEncoding();

}
