package org.matis.park.dto;

import org.matis.park.cmd.stdimp.CmdResponse;

/**
 * Created by manuel on 10/11/14.
 * <p>Fields in strict order using {@link org.matis.park.dto.Constants#LINE_SEP}</p>
 * <ul>
 *     <li>Field: app code, type int</li>
 *     <li>Field: app message, type string, optional. It does not contain the line separator never</li>
 * </ul>
 */
public class CmdResponseSerializer extends CmdResponseSerializerBase<CmdResponse> {

    /**
     * Transferable version
     */
    public static final int MAJOR_VERSION= 1;
    public static final int MINOR_VERSION= 0;

    public static final String CODE= "CMD_RESPONSE_SER";
    public static final int VERSION= 1;


    /**
     * Constructor
     */
    public CmdResponseSerializer()
    {
        super(CmdResponse.class);
    }

}
