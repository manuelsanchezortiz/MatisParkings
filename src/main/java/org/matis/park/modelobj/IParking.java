package org.matis.park.modelobj;

import java.util.Set;

/**
 * Created by manuel on 5/11/14.
 */
public interface IParking {

    public static final String FIELD_ID= "id";
    public static final String FIELD_NAME= "name";
    public static final String FIELD_OPENING_HOUR= "openingHour";
    public static final String FIELD_CLOSING_HOUR= "closingHour";
    public static final String FIELD_TOTAL_SLOTS= "totalSlots";
    public static final String FIELD_AVAILABLE_SLOTS= "availableSlots";
    public static final String FIELD_OPENING_DAYS= "openingDays";
    public static final String FIELD_GPS_LAT= "gpsLat";
    public static final String FIELD_GPS_LONG= "gpsLong";

    /**
     * Id field is mandatory
     * @return id
     */
    Integer getId();

    void setId(Integer id);

    String getName();

    void setName(String name);

    Integer getOpeningHour();

    void setOpeningHour(Integer openingHour);

    Integer getClosingHour();

    void setClosingHour(Integer closingHour);

    Integer getTotalSlots();

    void setTotalSlots(Integer totalSlots);

    Integer getAvailableSlots();

    void setAvailableSlots(Integer availableSlots);

    Set<Integer> getOpeningDays();

    void setOpeningDays(Set<Integer> openingDays);

    Float getGpsLat();

    void setGpsLat(Float gpsLat);

    Float getGpsLong();

    void setGpsLong(Float gpsLong);
}
