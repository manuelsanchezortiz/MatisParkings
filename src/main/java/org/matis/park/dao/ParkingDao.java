package org.matis.park.dao;

import org.matis.park.cmd.stdimp.CmdErrorCodes;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.modelobj.Parking;

import java.util.*;
import java.util.logging.Level;

import static org.matis.park.Logger.LOGGER;

/**
 * Created by manuel on 5/11/14.
 *
 * Basic mutithread support: sync on this on every object (read ones also to have consistent views)
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
    public synchronized CmdResponse insert(Parking p){

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Insert Parking: {0}", p);
        }

        CmdResponse cr= this.validate(p);

        if( cr.getAppCode() != CmdErrorCodes.NONE ){
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Insert response: {0}", cr);
            }
            return cr;
        }

        if( this.databaseMock.containsKey( p.getId())){

            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Insert response: {0}", cr);
            }
            cr= new CmdResponse(CmdErrorCodes.DUPLICATED, CmdErrorCodes.getMessage(CmdErrorCodes.DUPLICATED));

            return cr;
        }

        this.databaseMock.put(p.getId(), p);

        cr= CmdResponse.CMD_RESPONSE_OK;
        if( LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Insert response: {0}", cr);
        }

        return cr;
    }

    /**
     * Validate and update existing p. The id is used to replace the object which must exists
     * @param p
     * @return
     */
    public synchronized CmdResponse update(Parking p) {

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Update Parking: {0}", p);
        }

        if( p.getId() == null ){
            CmdResponse cr= new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Update response: {0}", cr);
            }
            return cr;
        }

        if( !this.databaseMock.containsKey( p.getId())){
            CmdResponse cr= new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, new Integer(p.getId()).toString() );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Update response: {0}", cr);
            }
            return cr;
        }

        CmdResponse cr= this.validate(p);

        if( cr.getAppCode() != CmdErrorCodes.NONE ){
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Update response: {0}", cr);
            }
            return cr;
        }

        //replace object
        this.databaseMock.put(p.getId(), p);

        cr= CmdResponse.CMD_RESPONSE_OK;

        if( LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Update response: {0}", cr);
        }
        return cr;

    }

    /**
     * Free slots, +1 on available slots up to total slots. As slots are optional, this action may do nothing
     *
     * @param id
     * @return
     */
    public synchronized CmdResponse freeSlot(Integer id) {

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Free slot id: {0}", id);
        }

        if( id == null ){
            //could have its own error
            CmdResponse cr= new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );

            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "FreeSlot response: {0}", cr);
            }
            return cr;
        }

        if( !this.databaseMock.containsKey( id )){

            CmdResponse cr= new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, id.toString() );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "FreeSlot response: {0}", cr);
            }
            return cr;
        }

        Parking p= this.databaseMock.get(id);

        if( p.getAvailableSlots() == null || p.getTotalSlots() == null ){

            CmdResponse cr=  CmdResponse.CMD_RESPONSE_OK;

            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "FreeSlot response: {0}", cr);
            }
            return cr;
        }

        p.setAvailableSlots( p.getAvailableSlots() + 1 );

        if( p.getAvailableSlots() > p.getTotalSlots() ){
            p.setAvailableSlots( p.getTotalSlots() );
            //could have its own error
            CmdResponse cr= new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );

            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "FreeSlot response: {0}", cr);
            }

            return cr;
        }

        CmdResponse cr= CmdResponse.CMD_RESPONSE_OK;

        if( LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "FreeSlot response: {0}", cr);
        }
        return cr;
    }

    /**
     * Use slots, -1 on available slots down to 0. As slots are optional, this action may do nothing
     *
     * @param id
     * @return
     */
    public synchronized CmdResponse useSlot(Integer id) {

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Use slot id: {0}", id);
        }


        if( id == null ){
            //could have its own error
            CmdResponse cr= new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Use slot response: {0}", cr);
            }

            return cr;
        }

        if( !this.databaseMock.containsKey( id )){
            CmdResponse cr= new CmdResponse( CmdErrorCodes.ENTITY_NOT_FOUND, id.toString() );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Use slot response: {0}", cr);
            }
            return cr;
        }

        Parking p= this.databaseMock.get(id);

        if( p.getAvailableSlots() == null || p.getTotalSlots() == null ){
            CmdResponse cr= CmdResponse.CMD_RESPONSE_OK;
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Use slot response: {0}", cr);
            }
            return cr;
        }

        p.setAvailableSlots( p.getAvailableSlots() - 1 );

        if( p.getAvailableSlots() < 0 ){
            p.setAvailableSlots( 0 );
            //could have its own error
            CmdResponse cr= new CmdResponse( CmdErrorCodes.NULL_OR_INVALID_FIELD, Parking.FIELD_ID );
            if( LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Use slot response: {0}", cr);
            }
            return cr;
        }

        CmdResponse cr= CmdResponse.CMD_RESPONSE_OK;
        if( LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Use slot response: {0}", cr);
        }
        return cr;

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
    public synchronized Parking getParkingWithId(int id){
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

        /**
         * For debugging and logging
         * @return
         */
        public String toString(){
            return "Found: " + (this.parkings == null ? 0 : this.parkings.size()) + " end=" + this.end;
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
    public synchronized QueryResult queryAll(int offset, int count, Filter filter){

        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Query all offset/count: {0}/{1}" + filter != null ? " Filtered" : "", new Object[]{offset, count});
        }

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

        QueryResult qr= new QueryResult( r, end);
        if( LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Query result: {0}" , qr);
        }

        return qr;
    }

}
