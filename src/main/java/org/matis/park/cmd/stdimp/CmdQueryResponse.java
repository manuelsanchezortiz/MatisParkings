package org.matis.park.cmd.stdimp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.matis.park.dto.TransferableObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by manuel on 8/11/14.
 *
 * A query response
 *
 */
public class CmdQueryResponse<T> extends CmdResponse {

    private int count;
    private boolean end;
    private Collection<T> objects;

    /**
     *
     * @return the count of objects in the response
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @return if the query has no more objects
     */
    public boolean isEnd() {
        return end;
    }

    /**
     *
     * @param appCode, application code
     * @param count, number or objects returned
     * @param end, if there isn't more
     * @param appMessage, application message
     */
    public CmdQueryResponse(int appCode, int count, boolean end, String appMessage, Collection<T> objects) {
        super(appCode, appMessage);

        this.count= count;
        this.end= end;

        //copy the collection avoid modification from outside
        this.objects= new ArrayList<T>(objects.size());
        this.objects.addAll(objects);

    }


    //TODO refactor CmdResponse & CmdQueryResponse to encode and decode themselves from is/os

    /**
     * <p>Encodes itself on an output stream.</p>
     * <ul>
     *     <li>One line with cmd status</li>
     *     <li>Then all the objects using the desired coder</li>
     * </ul>
     * @param coder, the coder to use
     */
    public String encode(TransferableObject<T> coder){



    }


}
