package org.matis.park.model;

import org.matis.park.cmd.stdimp.Haversine;

import java.text.DateFormatSymbols;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;

/**
 * Created by manuel on 5/11/14.
 * <p>The parking data object</p>
 */
public class Parking {

    public static final String FIELD_ID= "id";
    public static final String FIELD_NAME= "name";
    public static final String FIELD_OPENING_HOUR= "openingHour";
    public static final String FIELD_CLOSING_HOUR= "closingHour";
    public static final String FIELD_TOTAL_SLOTS= "totalSlots";
    public static final String FIELD_AVAILABLE_SLOTS= "availableSlots";
    public static final String FIELD_OPENING_DAYS= "openingDays";
    public static final String FIELD_GPS_LAT= "gpsLat";
    public static final String FIELD_GPS_LONG= "gpsLong";

    private Integer id;
    private String name;
    private Integer openingHour;
    private Integer closingHour;
    private Integer totalSlots;
    private Integer availableSlots;
    private Set<Integer> openingDays;
    private Float gpsLat, gpsLong;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    /**
     * Line separator not allowed
     * @param name, name of the parking
     */
    
    public void setName(String name) {

        if( name != null ){
            name= name.replace("\n", "").replace("\r", "");
        }

        this.name = name;
    }

    
    public Integer getOpeningHour() {
        return openingHour;
    }

    
    public void setOpeningHour(Integer openingHour) {
        this.openingHour = openingHour;
    }

    
    public Integer getClosingHour() {
        return closingHour;
    }

    
    public void setClosingHour(Integer closingHour) {
        this.closingHour = closingHour;
    }

    
    public Integer getTotalSlots() {
        return totalSlots;
    }

    
    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    
    public Integer getAvailableSlots() {
        return availableSlots;
    }

    
    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    
    public Set<Integer> getOpeningDays() {
        return openingDays;
    }

    
    public void setOpeningDays(Set<Integer> openingDays) {
        this.openingDays = openingDays;
    }

    
    public Float getGpsLat() {
        return gpsLat;
    }

    
    public void setGpsLat(Float gpsLat) {
        this.gpsLat = gpsLat;
    }

    
    public Float getGpsLong() {
        return gpsLong;
    }

    
    public void setGpsLong(Float gpsLong) {
        this.gpsLong = gpsLong;
    }



    /**
     * Test if this is opened on gc. If a need data is not present, we take the worst scenario. Validation will
     * check data but the method if safe
     * @param gc, date/time to check parking openness
     * @return if the parking is opened on gc
     */
    public boolean isOpened(GregorianCalendar gc){

        int currentDayOfWeek= gc.get( GregorianCalendar.DAY_OF_WEEK );

        boolean r= this.getOpeningDays() != null && this.getOpeningDays().contains( currentDayOfWeek );
        //also the time
        int currentHour= gc.get( GregorianCalendar.HOUR_OF_DAY );

        r= r && this.getOpeningHour() != null && this.getOpeningHour() <= currentHour;

        r= r && this.getClosingHour() != null && this.getClosingHour() >= currentHour;

        return r;
    }

    /**
     * Test if the parking is full, if data is not present result is the worst scenario: true. Validation will
     * check data but the method if safe
     * @return whether the parking is full or not
     */
    public boolean isFull(){

        return this.getAvailableSlots() == null || this.getAvailableSlots() == 0;
    }

    /**
     * Near is about 500 meters
     */
    public static final float NEAR_METERS= 500;

    /**
     * Test if this is near. If data is missing, worst scenario, it's not near. We only check float values
     * are near. In a real app. we need to do some maths.(or DB will do it for us)
     *
     * @param lat, latitude in degrees
     * @param longitude, longitude in degrees
     * @return if the gps coordinates are near of this using the epsilon {@link #NEAR_METERS}
     */
    public boolean isNear(float lat, float longitude) {

        if( this.getGpsLat() == null || this.getGpsLong() == null ){
            return false;
        }

        double d= Haversine.haversine( this.getGpsLat(), this.getGpsLong(), lat, longitude  );

        return d < NEAR_METERS;

    }

    //hash code is also painful without third party libs, i do no need it but is a good practice to implement when you do the equals

    /*
 * Error comparing gps coordinates; this has to be coherent with encoders/decoders precision
 */
    public static final float EPSILON= 0.00000001f;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parking parking = (Parking) o;

        if (availableSlots != null ? !availableSlots.equals(parking.availableSlots) : parking.availableSlots != null)
            return false;
        if (closingHour != null ? !closingHour.equals(parking.closingHour) : parking.closingHour != null) return false;
        if (gpsLat != null ? !(Math.abs( this.gpsLat - parking.gpsLat ) < EPSILON) : parking.gpsLat != null) return false;
        if (gpsLong != null ? !(Math.abs( this.gpsLong - parking.gpsLong ) < EPSILON) : parking.gpsLong != null) return false;
        if (!id.equals(parking.id)) return false;
        if (name != null ? !name.equals(parking.name) : parking.name != null) return false;
        if (openingDays != null ? !openingDays.equals(parking.openingDays) : parking.openingDays != null) return false;
        if (openingHour != null ? !openingHour.equals(parking.openingHour) : parking.openingHour != null) return false;
        return !(totalSlots != null ? !totalSlots.equals(parking.totalSlots) : parking.totalSlots != null);

    }


//    public boolean equals(Object o){
//
//        if( o != null && o instanceof  Parking ){
//            Parking p= (Parking)o;
//
//            boolean e= this.getId() != null && p.getId() != null &&  this.getId().equals( p.getId() );
//
//            e= e && (this.getName() == null && p.getName() == null) || (this.getName() != null && p.getName() != null && this.getName().equals( p.getName() ));
//            e= e && (this.getOpeningHour() == null && p.getOpeningHour() == null ) || (this.getOpeningHour() != null && p.getOpeningHour() != null && this.getOpeningHour().equals( p.getOpeningHour() ));
//            e= e && (this.getClosingHour() == null && p.getClosingHour() == null) || (this.getClosingHour() != null && p.getClosingHour() != null && this.getClosingHour().equals( p.getClosingHour() ));
//            e= e && (this.getTotalSlots() == null && p.getTotalSlots() == null) || (this.getTotalSlots() != null && p.getTotalSlots() != null && this.getTotalSlots().equals( p.getTotalSlots() ));
//            e= e && (this.getAvailableSlots() == null && p.getAvailableSlots() == null) || (this.getAvailableSlots() != null && p.getAvailableSlots() != null && this.getAvailableSlots().equals( p.getAvailableSlots() ));
//            e= e && (this.getOpeningDays() == null && p.getOpeningDays() == null) || (this.getOpeningDays() != null && p.getOpeningDays() != null && this.getOpeningDays().equals( p.getOpeningDays() ));
//            e= e && (this.getGpsLat() == null && p.getGpsLat() == null) || (this.getGpsLat() != null && p.getGpsLat() != null && Math.abs( this.getGpsLat() - p.getGpsLat() ) < EPSILON);
//            e= e && (this.getGpsLong() == null && p.getGpsLong() == null) || this.getGpsLong() != null && p.getGpsLong() != null && Math.abs( this.getGpsLong() - p.getGpsLong() ) < EPSILON;
//
//            return e;
//
//        }
//
//        return false;
//    }

    /**
     * Best practices, when implementing equals, add a hashcode
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (openingHour != null ? openingHour.hashCode() : 0);
        result = 31 * result + (closingHour != null ? closingHour.hashCode() : 0);
        result = 31 * result + (totalSlots != null ? totalSlots.hashCode() : 0);
        result = 31 * result + (availableSlots != null ? availableSlots.hashCode() : 0);
        result = 31 * result + (openingDays != null ? openingDays.hashCode() : 0);
        result = 31 * result + (gpsLat != null ? gpsLat.hashCode() : 0);
        result = 31 * result + (gpsLong != null ? gpsLong.hashCode() : 0);
        return result;
    }

    /**
     * String rep. (suitable for debug)
     * @return a string rep. of this
     */
    public String toString(){
        StringBuilder sb= new StringBuilder();

        sb.append("Id: ");
        sb.append(id);
        sb.append(", ");

        if( openingHour != null ){
            sb.append("Oh: ");
            sb.append(openingHour);
            sb.append(", ");
        }

        if( closingHour != null ){
            sb.append("Ch: ");
            sb.append(closingHour);
            sb.append(", ");
        }

        if( totalSlots != null ){
            sb.append("Ts: ");
            sb.append(totalSlots);
            sb.append(", ");
        }

        if( availableSlots != null ){
            sb.append("As: ");
            sb.append(availableSlots);
            sb.append(", ");
        }

        if( gpsLat != null ){
            sb.append("Lat: ");
            sb.append(gpsLat);
            sb.append(", ");
        }

        if( gpsLat != null ){
            sb.append("Long: ");
            sb.append(gpsLong);
        }

        if( openingDays != null ){
            sb.append(", Od: ");

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);

            int i= 0;
            for( int day: openingDays ){

                sb.append( symbols.getWeekdays()[day] );
                if( i < openingDays.size() -1 ) {
                    sb.append(", ");
                }
                i++;
            }
        }

        return sb.toString();
    }

}