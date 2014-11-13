package org.matis.park.cmd.stdimp;

import org.matis.park.modelobj.Parking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by manuel on 8/11/14.
 *
 * A query response
 *
 */
public class CmdQueryResponse extends CmdResponse {

    private int count;
    private boolean end;
    private List<Parking> parkings;

    /**
     *
     * @return the count of objects in the response
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count){
        this.count= count;
    }

    /**
     *
     * @return if the query has no more objects
     */
    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean b){
        this.end= end;
    }

    public List<Parking> getParkings(){
        return this.parkings;
    }

    public void setParkings(List<Parking> parkings){
        this.parkings= new ArrayList<Parking>(parkings);
    }

    /**
     * Used on serializers
     */
    public CmdQueryResponse(){

    }

    /**
     *
     * @param appCode, application code
     * @param count, number or objects returned
     * @param end, if there isn't more
     * @param appMessage, application message
     */
    public CmdQueryResponse(int appCode, int count, boolean end, String appMessage, Collection<Parking> parkings) {
        super(appCode, appMessage);

        this.count= count;
        this.end= end;

        //copy the collection avoid modification from outside
        this.parkings= new ArrayList<Parking>(parkings);
    }

    public String toString(){

        StringBuilder sb= new StringBuilder( super.toString());



        return sb.toString();
    }
}
