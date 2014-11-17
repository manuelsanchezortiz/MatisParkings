package org.matis.park.dto;

import org.matis.park.Utils;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.server.util.ParkException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

import static org.matis.park.dto.Constants.LINE_SEP;

/**
 * Created by manuel on 10/11/14.
 * <p>Fields in strict order using {@link org.matis.park.dto.Constants#LINE_SEP}</p>
 * <ul>
 *     <li>Field: app code, type int</li>
 *     <li>Field: app message, type string, optional. It does not contain the line separator never</li>
 * </ul>
 */
public abstract class CmdResponseSerializerBase<T extends CmdResponse> implements StringSerializer<T> {

    public static final String CODE= "CMD_RESPONSE_SER";


    private Class<T> crClazz;

    /**
     * Constructor with the class to instantiate
     * @param crClazz, the class to instantiate
     */
    public CmdResponseSerializerBase(Class<T> crClazz){
        this.crClazz= crClazz;
    }

    @Override
    public void encode(T o, BufferedWriter w) {

        PrintWriter pw= new PrintWriter(w);

        //do not use println as is platform dependent

        pw.print(o.getAppCode());
        pw.print(LINE_SEP);
        if( o.getAppMessage() != null ){
            //no LINE_SEP allowed
            String s= o.getAppMessage().replace(LINE_SEP, ' ');
            pw.print(s);
        }
        pw.print(LINE_SEP);

        pw.flush();

    }

    /**
     * We read the input stream line by line, we consume all lines as per field
     * count, the steam may contain more objects
     *
     * @param r, data input
     * @return the decoded object
     */
    @Override
    public T decode( BufferedReader r) {

        T cr;

        try {
            cr= this.crClazz.newInstance();

            //read each data in strict order
            String line = r.readLine();
            if(!Utils.isEmpty(line)){
                cr.setAppCode( Integer.parseInt(line) );
            }else{
                //id is mandatory, else null
                return null;
            }

            line = r.readLine();
            if(!Utils.isEmpty(line)){
                cr.setAppMessage(line);
            }
        } catch(Throwable e){
            throw new ParkException(e);
        }

        return cr;

    }
}
