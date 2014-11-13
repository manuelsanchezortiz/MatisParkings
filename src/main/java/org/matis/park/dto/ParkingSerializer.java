package org.matis.park.dto;

import com.sun.xml.internal.fastinfoset.stax.events.Util;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.ParkException;
import org.matis.park.util.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashSet;

import static org.matis.park.Constants.LINE_SEP;

/**
 * Created by manuel on 6/11/14.
 * <p>Parking serializer: encoding/decoding policy</p>
 * <p>This policy is </p>
 * <li>Encode numeric data as text, currently int and float; formatted as a 0.00000000 are supported</li>
 * <li>Write each field in sequence (strict order) using a \n as a delimiter</li>
 * <li>Set field is encoded as comma separated list of int</li>
 * <li>As order is strict, there is no need of wrapping string with "</li>
 * <li>String does not allow \n as it's used as a delimiter</li>
 * <li>Empty fields at least have an empty line</li>
 * <li>If id is empty we consider a null object </li>
 * <li>decimal point is always .</li>
 * </p>
 */
public class ParkingSerializer implements StringSerializer<Parking> {

    /**
     * Transferable version
     */
    public static final int MAJOR_VERSION= 1;
    public static final int MINOR_VERSION= 0;

    public static final String CODE= "PARKING_SER";
    public static final int VERSION= 1;

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion(){
        return MINOR_VERSION;
    }

    @Override
    public void encode(Parking o, BufferedWriter w) {

        PrintWriter pw= new PrintWriter(w);

        //do not use println as is platform dependent

        if( o.getId() !=  null) {
            pw.print(o.getId());
        }
        pw.print(LINE_SEP);
        if( o.getName() != null ) {
            pw.print(o.getName());
        }
        pw.print(LINE_SEP);
        if( o.getOpeningHour() != null) {
            pw.print(o.getOpeningHour());
        }
        pw.print(LINE_SEP);
        if( o.getClosingHour() != null ) {
            pw.print(o.getClosingHour());
        }
        pw.print(LINE_SEP);
        if( o.getTotalSlots() != null ) {
            pw.print(o.getTotalSlots());
        }
        pw.print(LINE_SEP);
        if( o.getAvailableSlots() != null ) {
            pw.print(o.getAvailableSlots());
        }
        pw.print(LINE_SEP);

        if( o.getGpsLat() != null ) {
            pw.print(Utils.DECIMAL_FORMAT.format(o.getGpsLat()));
        }
        pw.print(LINE_SEP);
        if( o.getGpsLong() != null ) {
            pw.print(Utils.DECIMAL_FORMAT.format(o.getGpsLong()));
        }
        pw.print(LINE_SEP);

        if( o.getOpeningDays() != null ){
            int i = 0;
            for (Integer d : o.getOpeningDays()) {
                pw.print(d);
                if (i < o.getOpeningDays().size() - 1) {
                    pw.print(',');
                }
                i++;
            }
        }
        pw.print(LINE_SEP);

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
    public Parking decode( BufferedReader r) {

        Parking p= null;

        try {

            p= new Parking();

            //read each data in strict order
            String line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setId( Integer.parseInt(line) );
            }else{
                //id is mandatory, else null
                return null;
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setName(line);
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setOpeningHour(Integer.parseInt(line));
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setClosingHour(Integer.parseInt(line));
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setTotalSlots( Integer.parseInt(line) );
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setAvailableSlots( Integer.parseInt(line) );
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                Number n= Utils.DECIMAL_FORMAT.parse(line);
                if( n != null ) {
                    p.setGpsLat(n.floatValue());
                }
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                Number n= Utils.DECIMAL_FORMAT.parse(line);
                if( n != null ) {
                    p.setGpsLong(n.floatValue());
                }
            }

            line = r.readLine();
            if(!Util.isEmptyString(line)){
                p.setOpeningDays(new HashSet<Integer>(5));
                String[] days= line.split(",");
                for( String day: days ){
                    p.getOpeningDays().add( Integer.parseInt(day));
                }
            }


        } catch(Throwable e){
            throw new ParkException(e);
        }

        return p;

    }
}
