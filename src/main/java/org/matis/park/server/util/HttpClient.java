package org.matis.park.server.util;

import org.matis.park.Utils;
import org.matis.park.cmd.stdimp.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 * <p>This is an http client used to write test cases at the moment</p>
 */
public class HttpClient {

    private static final String HEADER_CMD_VERSION= "CMD_VERSION";

    /**
     * Service version string
     */
    public static final String SERVICE_VERSION= "001";

    public static final int DEFAULT_PORT= 8080;

    private String urlBase = "http://localhost:" + DEFAULT_PORT;

    /**
     * Use default url for this client
     */
    public HttpClient() {
    }

    /**
     * <p>Use another url, protocol and port must be included, for example</p>
     * <code>http://localhost:8080</code>
     * @param url, url to use, set it when different from the default one which is <code>http://localhost:&gt;{@link #DEFAULT_PORT}&lt;</code>
     */
    public HttpClient(String url) {
        this.urlBase = url;
    }

    /**
     * Send a post request using a writer to upload data. The response result is returned
     *
     * @param cmd, the command to execute
     * @param payload, data encoded as a string
     * @return the response, http status and may a message (may be null)
     * @throws Exception
     */
    public Response sendPost(String cmd, String payload) throws Exception {

        return this.sendPost(cmd, payload, SERVICE_VERSION);

    }

    /**
     * Send post with specific version, use only for testing as version must always be. Use {@link #sendPost(String, String, String)}
     * @param cmd
     * @param payload
     * @param version
     * @return
     * @throws Exception
     */
    public Response sendPost(String cmd, String payload, String version) throws Exception {

        String url = urlBase;
        url += Constants.CTX + "/" + cmd;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod( HttpMethod.POST.name());
        if( !Utils.isEmpty(version)){
            con.setRequestProperty( HEADER_CMD_VERSION  , version );
        }
        con.setDoInput(true);
        con.setDoOutput(true);
        con.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        con.getOutputStream().flush();
        con.getOutputStream().close();

        InputStream is= con.getResponseCode() < 400 ? con.getInputStream() : con.getErrorStream();

        return new Response(con.getResponseCode(), is);

    }

    /**
     * Send a get request
     *
     * @param cmd,        command to execute
     * @param params,     for the get
     * @return the response, http status and may a message (may be null)
     * @throws IOException
     */
    public Response sendGet(String cmd, Map<String, Object> params) throws IOException {

        String url = urlBase;
        url += Constants.CTX + "/" + cmd;

        String paramsString = Utils.encodeParamsAsQueryString(params);
        if (!Utils.isEmpty(paramsString)) {
            url += "?" + paramsString;
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod(HttpMethod.GET.name());
        con.setRequestProperty( HEADER_CMD_VERSION  , SERVICE_VERSION );

        InputStream is= con.getResponseCode() < 400 ? con.getInputStream() : con.getErrorStream();

        Response r = new Response(con.getResponseCode(), is);

        return r;
    }

    /**
     * Response: http status and response (a string)
     */
    public static class Response {
        private int httpStatus;
        private InputStream inputStream;

        private Response(int httpStatus, InputStream inputStream) {
            this.httpStatus = httpStatus;
            this.inputStream= inputStream;
        }

        /**
         * Http status
         *
         * @return http status
         */
        public int getHttpStatus() {
            return httpStatus;
        }

        /**
         * Response is
         *
         * @return the input stream in the response
         */
        public InputStream getInputStream() {
            return inputStream;
        }

        /**
         * Most used UTF-8 buffered reader
         * @return a buffered reader with the input stream using utf-8
         */
        public BufferedReader getBufferedReader(){
            return new BufferedReader( new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
        }
    }

}
