package org.matis.park.dto;

import org.matis.park.cmd.stdimp.CmdQueryResponse;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.model.Parking;
import org.matis.park.server.util.ParkException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by manuel on 10/11/14.
 */
public class TransferUtil {

    /**
     * Encode a parking as a string
     * @param parking
     * @return string rep. of the parking
     */
    public static String parkingToString(Parking parking){

        ParkingSerializer ser= new ParkingSerializer();
        StringWriter sw= new StringWriter();
        BufferedWriter bw= new BufferedWriter(sw);
        ser.encode(parking, bw);
        try {
            bw.flush();
        } catch (IOException e) {
            throw new ParkException(e);
        }

        return sw.toString();
    }

    /**
     * Encode cmd response as a string
     * @param cmdResponse
     * @return string representation of the response
     */
    public static String cmdResponseToString(CmdResponse cmdResponse){

        CmdResponseSerializer ser= new CmdResponseSerializer();
        StringWriter sw= new StringWriter();
        BufferedWriter bw= new BufferedWriter(sw);
        ser.encode(cmdResponse, bw);
        try {
            bw.flush();
        } catch (IOException e) {
            throw new ParkException(e);
        }

        return bw.toString();
    }

    /**
     * Encode cmd query response as string
     * @param cmdQueryResponse
     * @return string rep. of a {@link org.matis.park.cmd.stdimp.CmdQueryResponse}
     */
    public static String cmdQueryResponseToString(CmdQueryResponse cmdQueryResponse){

        CmdQueryResponseSerializer ser= new CmdQueryResponseSerializer();
        StringWriter sw= new StringWriter();
        BufferedWriter bw= new BufferedWriter(sw);
        ser.encode(cmdQueryResponse, bw);
        try {
            bw.flush();
        } catch (IOException e) {
            throw new ParkException(e);
        }

        return bw.toString();
    }


}
