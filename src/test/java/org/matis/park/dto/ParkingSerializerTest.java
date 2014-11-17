package org.matis.park.dto;

import org.junit.Test;
import org.matis.park.model.Parking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ParkingSerializerTest {

    @Test
    public void testEncode() throws Exception {

        Parking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY)));

        ParkingSerializer s= new ParkingSerializer();

        StringWriter sw= new StringWriter();
        BufferedWriter w= new BufferedWriter( sw );

        s.encode(p, w);

        //decode it,test if fields match
        StringReader sr= new StringReader( sw.toString() );
        Parking newP= s.decode(new BufferedReader(sr));

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

        Parking p= new Parking();
        ParkingSerializer s= new ParkingSerializer();

        StringWriter sw= new StringWriter();
        BufferedWriter w= new BufferedWriter( sw );

        s.encode(p, w);
        w.flush();

        //decode it,test if fields match
        StringReader sr= new StringReader( sw.toString() );
        Parking newP= s.decode(new BufferedReader(sr));

        //decode it,test if fields match
        assertTrue( newP == null);

    }

    @Test
    public void whenIdFieldIsEmptyWeEncodeDecodeANullObject() throws Exception {

        //id is mandatory so this value as null means null object, also all blank fields
        Parking p= new Parking();
        //no id
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY)));

        ParkingSerializer s= new ParkingSerializer();

        StringWriter sw= new StringWriter();
        BufferedWriter w= new BufferedWriter( sw );

        s.encode(p, w);
        w.flush();

        StringReader sr= new StringReader( sw.toString() );
        Parking newP= s.decode(new BufferedReader(sr));

        //with no id must be null
        assertTrue( newP == null);

    }

    @Test
    public void whenOpeningDaysIsNullDecodingDoesNotThrownErrorItRemainsNull() throws Exception {

        Parking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        //no opening days

        ParkingSerializer s= new ParkingSerializer();

        StringWriter sw= new StringWriter();
        BufferedWriter w= new BufferedWriter( sw );

        s.encode(p, w);

        //decode it,test if fields match
        StringReader sr= new StringReader( sw.toString() );

        Parking newP= s.decode(new BufferedReader(sr));

        assertFalse( newP == null );
        assertTrue( newP.getOpeningDays() == null);

    }


    public void whenQueryingARangeGetGoodResults() throws Exception {





    }
}