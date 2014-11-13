package org.matis.park.dto;

import org.matis.park.cmd.stdimp.CmdQueryResponse;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.ParkException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by manuel on 10/11/14.
 */
public class TransferUtil {

    //TODO think about plugable ???

    /**
     * Encode a parking as a string
     * @param parking
     * @return
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
     * @return
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
     * @return
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
