package org.matis.park.dao;

import org.matis.park.cmd.stdimp.CmdErrorCodes;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.modelobj.Parking;

import java.util.*;

/**
 * Created by manuel on 5/11/14.
 *
 * Improvements: Support partial object update (patch)
 */
public class ParkingDao {

    /**
     * Simulated storage: A tree map which auto sorts by key (parking id)
     */
    private Map<Integer, Parking> databaseMock= new TreeMap<Integer, Parking>();

    /**
     * Insert p
     * @param p, new parking
     * @return ok, duplicated or an invalid field response
     */
    public CmdResponse insert(Parking p){

        CmdResponse cr= this.validate(p);

        if( cr.getAppCode() != CmdErrorCodes.NONE ){
            return cr;
        }

        if( this.databaseMock.containsKey( p.getId())){
            return new CmdResponse(CmdErrorCodes.DUPLICATED, CmdErrorCodes.getMessage(CmdErrorCodes.DUPLICATED));
        }

        this.databaseMock.put(p.getId(), p);

        return CmdResponse.CMD_RESPONSE_OK;
    }

    /**
     * Validate and update existing p. The id is used to replace the object which must exists
     * @param p
     * @return
     */
    public CmdResponse update(Parking p) {

        if( p.getId() == null ){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        if( !this.databaseMock.containsKey( p.getId())){
            return new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, new Integer(p.getId()).toString() );
        }

        CmdResponse cr= this.validate(p);

        if( cr.getAppCode() != CmdErrorCodes.NONE ){
            return cr;
        }

        //replace object
        this.databaseMock.put(p.getId(), p);

        return CmdResponse.CMD_RESPONSE_OK;

    }

    /**
     * Free slots, +1 on available slots up to total slots. As slots are optional, this action may do nothing
     *
     * @param id
     * @return
     */
    public CmdResponse freeSlot(Integer id) {

        if( id == null ){
            //could have its own error
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        if( !this.databaseMock.containsKey( id )){
            return new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, id.toString() );
        }

        Parking p= this.databaseMock.get(id);

        if( p.getAvailableSlots() == null || p.getTotalSlots() == null ){
            return CmdResponse.CMD_RESPONSE_OK;
        }

        p.setAvailableSlots( p.getAvailableSlots() + 1 );

        if( p.getAvailableSlots() > p.getTotalSlots() ){
            p.setAvailableSlots( p.getTotalSlots() );
            //could have its own error
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        return CmdResponse.CMD_RESPONSE_OK;
    }

    /**
     * Use slots, -1 on available slots down to 0. As slots are optional, this action may do nothing
     *
     * @param id
     * @return
     */
    public CmdResponse useSlot(Integer id) {


        if( id == null ){
            //could have its own error
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        if( !this.databaseMock.containsKey( id )){
            return new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, id.toString() );
        }

        Parking p= this.databaseMock.get(id);

        if( p.getAvailableSlots() == null || p.getTotalSlots() == null ){
            return CmdResponse.CMD_RESPONSE_OK;
        }

        p.setAvailableSlots( p.getAvailableSlots() - 1 );

        if( p.getAvailableSlots() < 0 ){
            p.setAvailableSlots( 0 );
            //could have its own error
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        return CmdResponse.CMD_RESPONSE_OK;

    }


    /**
     * <p>Validates parking, use this before insert/update operation</p>
     * <ul>
     *     <li>Id is mandatory. This is the only one</li>
     *     <li>Slots must be &gt; 0</li>
     *     <li>Availability must be &gt;= 0 and &lt; slots  </li>
     *     <li>Coordinates may seem coordinates</li>
     *     <li>We allow a parking to not open any day, we could have test more things...</li>
     * </ul>
     *
     * @param p
     * @return a cmd response
     */
    public CmdResponse validate( Parking p){

        //Test id, is mandatory
        if( p.getId() == null ){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
        }

        if( p.getTotalSlots() != null &&  p.getTotalSlots() < 0 ){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_TOTAL_SLOTS );
        }

        if( p.getAvailableSlots() != null && !( p.getAvailableSlots() >= 0 && p.getAvailableSlots() < p.getTotalSlots()  )){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_AVAILABLE_SLOTS );
        }

        if( p.getGpsLat() != null && !(p.getGpsLat() >= -90 && p.getGpsLat() <= 90) ){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_GPS_LAT );
        }

        if( p.getGpsLong() != null && !(p.getGpsLong() >= -180 && p.getGpsLong() <= 180) ){
            return new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_GPS_LONG );
        }

        return CmdResponse.CMD_RESPONSE_OK;
    }

    /**
     *
     * @param id, of the parking to retrieve
     * @return the parking with id of null if not found
     */
    public Parking getParkingWithId(int id){
        return this.databaseMock.get(id);
    }

    public static class QueryResult{
        private List<Parking> parkings;
        private boolean end;

        public List<Parking> getParkings() {
            return parkings;
        }

        public boolean isEnd() {
            return end;
        }

        private QueryResult(List<Parking> parkings, boolean end) {
            this.parkings = parkings;
            this.end = end;
        }
    }

    /**
     * Filter
     */
    public interface Filter {

        /**
         * p is accepted on results or not
         * @param p
         * @return
         */
        public boolean accept(Parking p);

    }

    /**
     * Returns a sorted list, by id, starting at offset and retrieving count objects
     * @param offset, from 0
     * @param count, number of items to return
     * @return
     */
    public QueryResult queryAll(int offset, int count, Filter filter){

        List<Parking> r= new ArrayList<Parking>(count);
        Collection<Parking> parkings= this.databaseMock.values();

        int i= 0;
        for( Parking parking: parkings ){

            if( i>= offset ){

                if( filter == null || filter.accept(parking )){
                    r.add(parking);
                }
            }

            i++;
            if( r.size() == count ){
                break;
            }
        }
        boolean end= i == parkings.size();

        return new QueryResult( r, end);
    }

}
