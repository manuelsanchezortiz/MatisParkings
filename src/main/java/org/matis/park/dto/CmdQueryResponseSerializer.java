package org.matis.park.dto;

import com.sun.xml.internal.fastinfoset.stax.events.Util;
import org.matis.park.cmd.stdimp.CmdQueryResponse;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.ParkException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.matis.park.Constants.LINE_SEP;

/**
 * Created by manuel on 10/11/14.
 * <p>Fields in strict order using {@link org.matis.park.Constants#LINE_SEP}</p>
 * <ul>
 *     <li>Field: app code, type int</li>
 *     <li>Field: app message, type string, optional. It does not contain the line separator never</li>
 *     <li>Field: count, type int</li>
 *     <li>Field: end, type string(1) T/F for true/false</li>
 *     <li>Field: objects, the list of string serializable objects</li>
 * </ul>
 */
public class CmdQueryResponseSerializer extends CmdResponseSerializerBase<CmdQueryResponse> {

    public static final String CODE= "CMD_QUERY_RESPONSE_SER";
    public static final int VERSION= 1;

    private static final String TRUE= "T";
    private static final String FALSE= "F";

    /**
     * Constructor with the class to instantiate
     */
    public CmdQueryResponseSerializer(){
        super(CmdQueryResponse.class);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public void encode(CmdQueryResponse o, BufferedWriter w) {

        super.encode(o, w);

        PrintWriter pw= new PrintWriter(w);

        //do not use println as is platform dependent
        pw.print( o.getCount() );
        pw.print(LINE_SEP);
        pw.print( o.isEnd() ? TRUE : FALSE );
        pw.print(LINE_SEP);

        ParkingSerializer ps= new ParkingSerializer();
        for(Parking parking: o.getParkings()){

            ps.encode( parking, w);
        }


        pw.flush();

    }

    /**
     * We read the input stream line by line, we consume all lines as per field
     * count, the steam may contain more objects
     *
     * @param r, data input
     * @return
     */
    @Override
    public CmdQueryResponse decode( BufferedReader r) {

        CmdQueryResponse cqr = super.decode(r);

        try {
            //read each data in strict order
            String line = r.readLine();
            if (!Util.isEmptyString(line)) {
                cqr.setCount(Integer.parseInt(line));
            } else {
                //id is mandatory, else null
                return null;
            }

            line= r.readLine();
            if( !Util.isEmptyString(line)){
                cqr.setEnd( line.equals(TRUE)  );
            }else{
                throw new ParkException("Data format error");
            }


            ParkingSerializer ps = new ParkingSerializer();
            List<Parking> parkings = new ArrayList<Parking>(cqr.getCount());
            for (int i = 0; i < cqr.getCount(); i++) {

                Parking p = ps.decode(r);

                parkings.add(p);

            }
            cqr.setParkings(parkings);

        } catch (Throwable e) {
            throw new ParkException(e);
        }

        return cqr;
    }
}
