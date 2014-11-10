package org.matis.park.dto;

import com.sun.xml.internal.fastinfoset.stax.events.Util;
import org.matis.park.modelobj.IParking;
import org.matis.park.util.ParkException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;

import static org.matis.park.Constants.LINE_SEP;

/**
 * Created by manuel on 6/11/14.
 * <p>Transfer object: encoding/decoding policy</p>
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
public class TextParkingDto implements TransferableObject<IParking> {

    /**
     * Transferable version
     */
    public static final String VERSION= "1";

    public static final String CODE= "TextParkingDto";
    public static final int VERSION= 1;
    public static final String CONTENT_TYPE= "text/plain";
    private static final DecimalFormat DECIMAL_FORMAT= new DecimalFormat("0.00000000");

    static {
        //assure . always
        DecimalFormatSymbols dfs= DecimalFormatSymbols.getInstance(Locale.ENGLISH );
        dfs.setDecimalSeparator('.');
        DECIMAL_FORMAT.setDecimalFormatSymbols( dfs );
    }

    private int version;

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public String getEncoding() {
        return StandardCharsets.UTF_8.displayName();
    }



    @Override
    public void encode(IParking o, OutputStream os) {

        OutputStreamWriter w= null;
        try {
            w = new OutputStreamWriter(os, this.getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new ParkException(e);
        }
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
            pw.print(DECIMAL_FORMAT.format(o.getGpsLat()));
        }
        pw.print(LINE_SEP);
        if( o.getGpsLong() != null ) {
            pw.print(DECIMAL_FORMAT.format(o.getGpsLong()));
        }
        pw.print(LINE_SEP);

        if( o.getOpeningDays() != null ){
            int i = 0;
            for (Integer integer : o.getOpeningDays()) {
                pw.print(i);
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
     * @param tParking, desired return type
     * @param is, data input
     * @return
     */
    @Override
    public IParking decode(Class<? extends IParking> tParking, InputStream is) {

        IParking p= null;

        //TODO manage throws
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is, this.getEncoding()));

            p= tParking.newInstance();

            //read each data in strict order
            String line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setId( Integer.parseInt(line) );
            }else{
                //id is mandatory, else null
                return null;
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setName(line);
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setOpeningHour(Integer.parseInt(line));
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setClosingHour(Integer.parseInt(line));
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setTotalSlots( Integer.parseInt(line) );
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                p.setAvailableSlots( Integer.parseInt(line) );
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                Number n= DECIMAL_FORMAT.parse(line);
                if( n != null ) {
                    p.setGpsLat(n.floatValue());
                }
            }

            line = in.readLine();
            if(!Util.isEmptyString(line)){
                Number n= DECIMAL_FORMAT.parse(line);
                if( n != null ) {
                    p.setGpsLong(n.floatValue());
                }
            }

            line = in.readLine();
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
