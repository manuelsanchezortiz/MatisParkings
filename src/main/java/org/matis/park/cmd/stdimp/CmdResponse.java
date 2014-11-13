package org.matis.park.cmd.stdimp;

/**
 * Created by manuel on 6/11/14.
 * <p>Contains a command response (application scope), it's built by each command using the raw response from the server</p>
 */
public class CmdResponse {

    /**
     * Standard cmd response, message does not change
     */
    public static final CmdResponse CMD_RESPONSE_OK= new CmdResponse( CmdErrorCodes.NONE, CmdErrorCodes.getMessage(CmdErrorCodes.NONE) );

    /**
     * Standard cmd response, message does not change
     */
    public static final CmdResponse CMD_RESPONSE_INVALID= new CmdResponse( CmdErrorCodes.INVALID_RESPONSE, CmdErrorCodes.getMessage(CmdErrorCodes.INVALID_RESPONSE) );

    /**
     * Standard protocol error
     */
    public static final CmdResponse CMD_RESPONSE_PROTOCOL_ERROR= new CmdResponse( CmdErrorCodes.PROTOCOL_ERROR, CmdErrorCodes.getMessage(CmdErrorCodes.PROTOCOL_ERROR) );

    /**
     * Standard internal server error
     */
    public static final CmdResponse CMD_INTERNAL_SERVER_ERROR= new CmdResponse( CmdErrorCodes.PROTOCOL_ERROR, CmdErrorCodes.getMessage(CmdErrorCodes.PROTOCOL_ERROR) );


    private int appCode;
    private String appMessage;

    /**
     * For serialization
     */
    public CmdResponse(){

    }

    public CmdResponse(int appCode, String appMessage) {
        this.appCode = appCode;
        this.appMessage = appMessage;
    }

    public int getAppCode() {
        return appCode;
    }

    public void setAppCode( int appCode ){
        this.appCode= appCode;
    }

    public String getAppMessage() {
        return appMessage;
    }

    public void setAppMessage(String appMessage){
        this.appMessage= appMessage;
    }

    /**
     * Encode the response
     * @return the response with format appcode:appmessage
     */
    public String encode( ){

        StringBuilder sb= new StringBuilder();
        sb.append(this.getAppCode());
        sb.append(":");
        sb.append(this.getAppMessage());

        return sb.toString();
    }
}
