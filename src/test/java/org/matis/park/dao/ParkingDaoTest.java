package org.matis.park.dao;

import org.junit.Test;
import org.matis.park.cmd.stdimp.CmdErrorCodes;
import org.matis.park.cmd.stdimp.CmdResponse;
import org.matis.park.modelobj.IParking;
import org.matis.park.modelobj.Parking;

import static org.junit.Assert.assertTrue;

public class ParkingDaoTest {

    private IParking getAParking(){
        IParking p= new Parking();
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

    @Test
    public void whenInsertingDuplicatedValueThenError() throws Exception{

        ParkingDao parkingDao= new ParkingDao();

        IParking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.DUPLICATED );
    }

    @Test
    public void whenParkingIsInvalidThenError() throws Exception {

        ParkingDao parkingDao= new ParkingDao();

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

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

        IParking p= this.getAParking();

        CmdResponse cr= parkingDao.insert(p);

        assertTrue(cr.getAppCode() == CmdErrorCodes.NONE );

        while( cr.getAppCode() == CmdErrorCodes.NONE) {
            cr = parkingDao.useSlot(p.getId());
        }

        assertTrue(p.getAvailableSlots() == 0 );

    }

    //different kinds of queries
}