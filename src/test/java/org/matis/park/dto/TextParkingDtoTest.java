package org.matis.park.dto;

import org.junit.Test;
import org.matis.park.modelobj.IParking;
import org.matis.park.modelobj.Parking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.*;

public class TextParkingDtoTest {

    @Test
    public void testEncode() throws Exception {


        IParking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY)));

        TextParkingDto dto= new TextParkingDto();

        ByteArrayOutputStream os= new ByteArrayOutputStream();

        dto.encode(p, os);

        //decode it,test if fields match

        IParking newP= dto.decode(Parking.class, new ByteArrayInputStream( os.toByteArray()  ));

        assertEquals( p.getId(), newP.getId() );
        assertEquals( p.getName(), newP.getName() );
        assertEquals( p.getTotalSlots(), newP.getTotalSlots() );
        assertEquals( p.getAvailableSlots(), newP.getAvailableSlots() );
        assertEquals( p.getOpeningHour(), newP.getOpeningHour() );
        assertEquals( p.getClosingHour(), newP.getClosingHour() );
        assertEquals( p.getGpsLat(), newP.getGpsLat(), 0.00000001f );
        assertEquals( p.getGpsLong(), newP.getGpsLong(), 0.000001f );
        assertEquals( p.getOpeningDays(), newP.getOpeningDays() );
    }

    @Test
    public void whenNoFieldsHasDataWeEncodeDecodeANullObject() throws Exception {

        //id is mandatory so this value as null means null object, also all blank fields

        IParking p= new Parking();
        TextParkingDto dto= new TextParkingDto();

        ByteArrayOutputStream os= new ByteArrayOutputStream();

        dto.encode(p, os);

        //decode it,test if fields match

        IParking newP= dto.decode(Parking.class, new ByteArrayInputStream(os.toByteArray()));

        assertTrue( newP == null);

    }

    @Test
    public void whenIdFieldIsEmptyWeEncodeDecodeANullObject() throws Exception {

        //id is mandatory so this value as null means null object, also all blank fields
        IParking p= new Parking();
        //no id
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY)));

        TextParkingDto dto= new TextParkingDto();

        ByteArrayOutputStream os= new ByteArrayOutputStream();

        dto.encode(p, os);

        os.close();

        //decode it,test if fields match

        IParking newP= dto.decode(Parking.class, new ByteArrayInputStream( os.toByteArray()  ));

        assertTrue( newP == null);

    }

    @Test
    public void whenOpeningDaysIsNullDecodingDoesNotThrownErrorItRemainsNull() throws Exception {

        IParking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);

        TextParkingDto dto= new TextParkingDto();

        ByteArrayOutputStream os= new ByteArrayOutputStream();

        dto.encode(p, os);

        //decode it,test if fields match

        IParking newP= dto.decode(Parking.class, new ByteArrayInputStream( os.toByteArray()  ));

        assertFalse( newP == null );
        assertTrue( newP.getOpeningDays() == null);

    }

}