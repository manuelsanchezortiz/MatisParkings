package org.matis.park.util;

import org.matis.park.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by manuel on 6/11/14.
 */
public class HttpClient {

    //not needed ???
    private final String USER_AGENT = "Mozilla/5.0";
    private String urlbase = "http://localhost:8081";

    /**
     * Use default url
     */
    public HttpClient() {
    }

    public HttpClient(String url) {
        this.urlbase = url;
    }

    /**
     * Send a post request using a writer to upload data. The response result is returned
     *
     * @param cmd
     * @param payload, data enconded as a string
     * @return the response, http status and may a message (may be null)
     * @throws Exception
     */
    public Response sendPost(String cmd, String payload) throws Exception {

        String url = urlbase;
        url += Constants.CTX + "/" + cmd;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod( HttpMethod.POST.name());
        con.setDoInput(true);
        con.setDoOutput(true);
        con.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        con.getOutputStream().flush();
        con.getOutputStream().close();

        Response r = new Response(con.getResponseCode(), con.getInputStream());

        return r;

    }

    /**
     * Send a get request
     *
     * @param cmd,        command to execute
     * @param params,     for the get
     * @return the response, http status and may a message (may be null)
     * @throws Exception
     */
    public Response sendGet(String cmd, Map<String, Object> params) throws IOException {

        String url = urlbase;
        url += Constants.CTX + "/" + cmd;

        String paramsString = Utils.encodeParamsAsQueryString(params);
        if (!Utils.isEmpty(paramsString)) {
            url += "?" + paramsString;
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(HttpMethod.GET.name());
        Response r = new Response(con.getResponseCode(), con.getInputStream());

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
         * @return
         */
        public int getHttpStatus() {
            return httpStatus;
        }

        /**
         * Response is
         *
         * @return
         */
        public InputStream getInputStream() {
            return inputStream;
        }

        /**
         * Most used UTF-8 buffered reader
         * @return
         */
        public BufferedReader getBufferedReader(){
            return new BufferedReader( new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
        }
    }

}
