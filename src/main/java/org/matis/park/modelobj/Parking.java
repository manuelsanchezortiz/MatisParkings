package org.matis.park.modelobj;

import org.matis.park.Constants;

import java.util.Set;

/**
 * Created by manuel on 5/11/14.
 */
public class Parking implements IParking {

    //I like to always use english in code

    private Integer id;
    private String name;
    private Integer openingHour;
    private Integer closingHour;
    private Integer totalSlots;
    private Integer availableSlots;
    private Set<Integer> openingDays;
    private Float gpsLat, gpsLong;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Line separator not allowed
     * @param name
     */
    @Override
    public void setName(String name) {

        if( name != null ){
            name= name.replace(Constants.LINE_SEP, ' ');
        }

        this.name = name;
    }

    @Override
    public Integer getOpeningHour() {
        return openingHour;
    }

    @Override
    public void setOpeningHour(Integer openingHour) {
        this.openingHour = openingHour;
    }

    @Override
    public Integer getClosingHour() {
        return closingHour;
    }

    @Override
    public void setClosingHour(Integer closingHour) {
        this.closingHour = closingHour;
    }

    @Override
    public Integer getTotalSlots() {
        return totalSlots;
    }

    @Override
    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    @Override
    public Integer getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public Set<Integer> getOpeningDays() {
        return openingDays;
    }

    @Override
    public void setOpeningDays(Set<Integer> openingDays) {
        this.openingDays = openingDays;
    }

    @Override
    public Float getGpsLat() {
        return gpsLat;
    }

    @Override
    public void setGpsLat(Float gpsLat) {
        this.gpsLat = gpsLat;
    }

    @Override
    public Float getGpsLong() {
        return gpsLong;
    }

    @Override
    public void setGpsLong(Float gpsLong) {
        this.gpsLong = gpsLong;
    }

    /*
     * Error comparing gps coordinates; this has to be coherent with encoders/decoders precision
     */
    public static final float EPSILON= 0.00000001f;

    @Override
    public boolean equals(Object o){

        if( o != null && o instanceof  Parking ){
            Parking p= (Parking)o;

            boolean e= this.getId() != null && p.getId() != null &&  this.getId().equals( p.getId() );

            e= e && (this.getName() == null && p.getName() == null) || (this.getName() != null && p.getName() != null && this.getName().equals( p.getName() ));
            e= e && (this.getOpeningHour() == null && p.getOpeningHour() == null ) || (this.getOpeningHour() != null && p.getOpeningHour() != null && this.getOpeningHour().equals( p.getOpeningHour() ));
            e= e && (this.getClosingHour() == null && p.getClosingHour() == null) || (this.getClosingHour() != null && p.getClosingHour() != null && this.getClosingHour().equals( p.getClosingHour() ));
            e= e && (this.getTotalSlots() == null && p.getTotalSlots() == null) || (this.getTotalSlots() != null && p.getTotalSlots() != null && this.getTotalSlots().equals( p.getTotalSlots() ));
            e= e && (this.getAvailableSlots() == null && p.getAvailableSlots() == null) || (this.getAvailableSlots() != null && p.getAvailableSlots() != null && this.getAvailableSlots().equals( p.getAvailableSlots() ));
            e= e && (this.getOpeningDays() == null && p.getOpeningDays() == null) || (this.getOpeningDays() != null && p.getOpeningDays() != null && this.getOpeningDays().equals( p.getOpeningDays() ));
            e= e && (this.getGpsLat() == null && p.getGpsLat() == null) || (this.getGpsLat() != null && p.getGpsLat() != null && Math.abs( this.getGpsLat() - p.getGpsLat() ) < EPSILON);
            e= e && (this.getGpsLong() == null && p.getGpsLong() == null) || this.getGpsLong() != null && p.getGpsLong() != null && Math.abs( this.getGpsLong() - p.getGpsLong() ) < EPSILON;

            return e;

        }

        return false;
    }

    //hash code is also painful without third party libs, i do no need it but is a good practice to implement when you do the equals
}