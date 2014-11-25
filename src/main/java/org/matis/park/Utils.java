package org.matis.park;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 *
 * <p>Impl notes: Check methods throw exceptions or do nothing</p>
 */
public class Utils {

    /**
     * Test string emptiness
     * @param s, the string to test
     * @return true when s is null or 0 length or only contains space chars (we use trim before test)
     */
    public static boolean isEmpty(String s){

        return s == null || s.trim().length() == 0;

    }

    /**
     * Throw when doing a emptiness check
     */
    public static class EmptyException extends RuntimeException{
        /**
         *
         * @param msg, usually the field which is empty
         */
        public EmptyException(String msg){
            super(msg);
        }
    }

    /**
     * Test string emptiness, uses {@link #isEmpty(String)}
     * @param s, the string to check
     * @param msg, at least field which is empty, optional, but advisable
     */
    public static void checkNotEmpty(String s, String msg){
        if( isEmpty(s) ){
            throw new EmptyException(msg);
        }
    }

    /**
     * Test object nullity, to be consistent with other checks
     * @param s, the string to check
     * @param msg, at least field which is empty, optional, but advisable
     */
    public static void checkNotNull(Object s, String msg){
        if( s == null ){
            throw new EmptyException(msg);
        }
    }

    /**
     * Encode map as a query string suitable for http
     * @param params
     * @return
     */
    public static String encodeParamsAsQueryString(Map<String, Object> params) {

        if( params == null ){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue() == null ? "" : String.valueOf(entry.getValue());
            sb.append(separator);
            sb.append(urlEncode(entry.getKey()));
            sb.append('=');
            sb.append(urlEncode(value));
            separator = "&";
        }

        return sb.toString();
    }

    /**
     * url compatible encoding
     * @param value, to encode
     * @return value encoded as a url
     */
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * url compatible decoding
     * @param value, to decode
     * @return value decoded from url
     */
    public static String urlDecode(String value){
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * Decode a query string from a InputStream (utf-8)
     * @param is
     * @return
     */
    public static Map<String,String> decodeParamsFromQueryString( InputStream is ) throws IOException{

        BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String query = in.readLine();

        return decodeParamsFromQueryString(query);

    }

    /**
     * Decode a query string
     * @param query, query to parse
     * @return the params, those with no value has ""
     */
    public static Map<String,String> decodeParamsFromQueryString( String query ){

        if( Utils.isEmpty(query)){
            return null;
        }

        String params[]= query.split("&");

        Map<String,String> r= new HashMap<String, String>();

        for( String param: params){

            String keyValue[]= param.split("=");

            if( keyValue.length >1 ) {
                r.put(keyValue[0], urlDecode(keyValue[1]));
            }else{
                r.put(keyValue[0], "");
            }
        }

        return r;
    }

    public static final DecimalFormat DECIMAL_FORMAT= new DecimalFormat("0.00000000");

    static {
        //assure . always
        DecimalFormatSymbols dfs= DecimalFormatSymbols.getInstance(Locale.ENGLISH );
        dfs.setDecimalSeparator('.');
        DECIMAL_FORMAT.setDecimalFormatSymbols( dfs );
    }
}
