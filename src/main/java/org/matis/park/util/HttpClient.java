package org.matis.park.util;

import org.matis.park.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 */
public class HttpClient {

    private String urlbase= "http://localhost:8081";

    /**
     * Use default url
     */
    public HttpClient() {
    }

    public HttpClient(String url){
        this.urlbase= url;
    }

    //not needed ???
    private final String USER_AGENT = "Mozilla/5.0";

    private String buildParams(Map<String,String> params){
        return "";
    }

    /**
     * Callback object to write post data
     */
    public interface DataWriter {
        public void write(OutputStream os);
    }

    /**
     * Callback object to read data
     */
    public interface DataReader {
        public void read(InputStream is);
    }

    /**
     * Protocol response.. An Http status may be OK but
     * the response may give an app error like duplicate ids (client must inspect response for this delegating to
     * the command)
     *
     */
    public static class Response {
        private int httpStatus;
        private String response;

        private Response(int httpStatus, String response) {
            this.httpStatus = httpStatus;
            this.response = response;
        }

        /**
         * Http status
         * @return
         */
        public int getHttpStatus() {
            return httpStatus;
        }

        /**
         * Some response message, command dependent
         * @return
         */
        public String getResponse() {
            return response;
        }
    }

    /**
     * Send a post request using a writer to upload data. The response result is returned
     * @param cmd
     * @param dataWriter
     * @param dataReader, if null we read a string message into the response. Else we use data reader to read and
     *                    the response has no message
     * @return the response, http status and may a message (may be null)
     * @throws Exception
     */
    public Response sendPost(String cmd, DataWriter dataWriter, DataReader dataReader) throws Exception {

        String url =  urlbase;
        url+= Constants.CTX + "/" + cmd;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

        // Send post request
        if( dataWriter != null ) {
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            dataWriter.write(wr);
            wr.flush();
            wr.close();
        }

        Response r= this.readResponse( con.getResponseCode(), con.getInputStream(), dataReader);

        return r;

    }

    /**
     * Send a get request
     * @param cmd, command to execute
     * @param params, for the get
     * @param dataReader, if null we read a string message into the response. Else we use data reader to read and
     *                    the response has no message
     * @return the response, http status and may a message (may be null)
     * @throws Exception
     */
    public Response sendGet(String cmd, Map<String,String> params, DataReader dataReader) throws IOException {

        String url = urlbase;
        url += Constants.CTX + "/" + cmd;

        String paramsString = this.buildParams(params);
        if (!TestUtils.isEmpty(paramsString)){
            url += "?" + paramsString;
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        Response r= this.readResponse( con.getResponseCode(), con.getInputStream(), dataReader);

        return r;
    }

    /**
     * Creates a response from is. Without a data reader the response contains the responseCode and a message (String)
     * from the is. Else the response message is null and the reade do the job
     * @param responseCode
     * @param is
     * @param dataReader
     * @return
     * @throws IOException
     */
    private Response readResponse( int responseCode, InputStream is, DataReader dataReader) throws IOException{

        String responseMessage= null;

        if( dataReader != null ){
            dataReader.read(is);
        }else {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(is));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseMessage = response.toString();
        }

        Response r= new Response( responseCode, responseMessage );

        return r;
    }

}
