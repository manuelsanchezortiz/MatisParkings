package org.matis.park.dao;

import org.junit.Test;
import org.matis.park.cmd.stdimp.CmdErrorCodes;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.modelobj.Parking;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class ParkingDaoTest {

    private Parking getAParking(){
        Parking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(100);
        p.setAvailableSlots(10);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);

        return p;
    }

    private Collection<Parking> getParkings(){

        Collection<Parking> r= new ArrayList<Parking>(5);

        Parking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(1000);
        p.setAvailableSlots(560);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)));
        r.add( p );

        p= new Parking();
        p.setId(2);
        p.setName("Park02");
        p.setTotalSlots(3000);
        p.setAvailableSlots(2500);
        p.setOpeningHour(0);
        p.setClosingHour(0);
        p.setGpsLat(41.292f);
        p.setGpsLong(2.054f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY )));
        r.add( p );

        p= new Parking();
        p.setId(3);
        p.setName("Park03");
        p.setTotalSlots(500);
        p.setAvailableSlots(250);
        p.setOpeningHour(15);
        p.setClosingHour(3);
        p.setGpsLat(41.400736f);
        p.setGpsLong(2.172467f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY )));
        r.add( p );


        p= new Parking();
        p.setId(4);
        p.setName("Park04");
        p.setTotalSlots(50);
        p.setAvailableSlots(12);
        p.setOpeningHour(16);
        p.setClosingHour(4);
        p.setGpsLat(41.500736f);
        p.setGpsLong(2.272467f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY )));
        r.add( p );


        p= new Parking();
        p.setId(5);
        p.setName("Park05");
        p.setTotalSlots(1500);
        p.setAvailableSlots(50);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.300736f);
        p.setGpsLong(2.072467f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY )));
        r.add( p );

        return r;
    }

    @Test
    public void whenInsertingDuplicatedValueThenError() throws Exception{

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.DUPLICATED );
    }

    @Test
    public void whenParkingIsInvalidThenError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        p.setId(null);
        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NULL_OR_INVALID_FIELD );

        p= this.getAParking();

        p.setAvailableSlots(200);
        p.setTotalSlots(100);

        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NULL_OR_INVALID_FIELD );

        p= this.getAParking();

        p.setAvailableSlots(-1);

        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NULL_OR_INVALID_FIELD );

        p= this.getAParking();

        p.setGpsLat(1000.0f);
        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NULL_OR_INVALID_FIELD );

        p= this.getAParking();

        p.setGpsLong(-190.0f);
        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NULL_OR_INVALID_FIELD );
    }

    @Test
    public void whenTryingToUpdateMissingObjectThenError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        //update non existent
        p= this.getAParking();
        p.setId(200);

        cr= parkingDao.update(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );
    }

    @Test
    public void whenTryingToExecActionOnMissingObjectThenError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        cr= parkingDao.freeSlot(200);

        assertTrue(cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );

        cr= parkingDao.useSlot(300);

        assertTrue(cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );
    }

    @Test
    public void whenFreeingSlotThenAvailableSlotAugments() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        int av= p.getAvailableSlots();
        cr= parkingDao.freeSlot(p.getId());

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        assertTrue(p.getAvailableSlots() == av + 1 || p.getAvailableSlots() == p.getTotalSlots() );
    }

    @Test
    public void whenUsingSlotThenAvailableSlotReduces() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        int av= p.getAvailableSlots();
        cr= parkingDao.useSlot(p.getId());

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        assertTrue(p.getAvailableSlots() == av -1 || p.getAvailableSlots() == 0  );
    }

    @Test
    public void whenFreeingSlotsThenRemainsBetweenValidRangeAndReturnsError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        while( cr.getAppCode() == CmdErrorCodes.NONE) {
            cr = parkingDao.freeSlot(p.getId());
        }

        assertTrue(p.getAvailableSlots() <= p.getTotalSlots() );


    }

    @Test
    public void whenUsingSlotsThenRemainsBetweenValidRangeAndReturnsError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        Parking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        while( cr.getAppCode() == CmdErrorCodes.NONE) {
            cr = parkingDao.useSlot(p.getId());
        }

        assertTrue(p.getAvailableSlots() == 0 );

    }

    public void whenQueryingARangeGetGoodResults() throws Exception {


        ParkingDao parkingDao= new ParkingDao();

        Collection<Parking> parkings= this.getParkings();

        for( Parking parking: parkings){
            parkingDao.insert(parking);
        }

        int count= 2;
        int offset=3;
        ParkingDao.QueryResult qr= parkingDao.queryAll( offset,count, null);

        // 5 items, get index 3 so get Park04 and Park05

        assertTrue( "Must end, no more records", qr.isEnd() );
        assertTrue( qr.getParkings().size() == count);
        assertTrue( qr.getParkings().get(0).getId() == 4);
        assertTrue( qr.getParkings().get(1).getId() == 5);
    }

    public void whenQueryingARangeAndFilterIsActiveGetGoodResults() throws Exception {


        ParkingDao parkingDao= new ParkingDao();

        Collection<Parking> parkings= this.getParkings();

        for( Parking parking: parkings){
            parkingDao.insert(parking);
        }

        int count= 3;
        int offset=1;
        ParkingDao.QueryResult qr= parkingDao.queryAll( offset,count, new ParkingDao.Filter() {
            @Override
            public boolean accept(Parking p) {

                return p.getId() != 3;

            }
        });

        // 5 items, get index 3 so get Park04 and Park05

        assertTrue( "Must end, no more records", qr.isEnd() );
        assertTrue( qr.getParkings().size() == count);
        assertTrue( qr.getParkings().get(0).getId() == 2);
        assertTrue( qr.getParkings().get(1).getId() == 4);
        assertTrue( qr.getParkings().get(1).getId() == 5);
    }
}