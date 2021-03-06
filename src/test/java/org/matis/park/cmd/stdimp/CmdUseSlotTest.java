package org.matis.park.cmd.stdimp;

import org.junit.Test;
import org.matis.park.server.Server;
import org.matis.park.dto.CmdResponseSerializer;
import org.matis.park.model.Parking;
import org.matis.park.server.util.HttpClient;
import org.matis.park.server.util.HttpStatus;
import org.matis.park.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class CmdUseSlotTest {

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

    @Test
    public void whenUsingSlotAvailabilityIsReduced() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent
        final Parking p= this.getAParking();
        server.getParkingDao().insert( p );

        int availableSlots= p.getAvailableSlots();

        try {
            HttpClient c = new HttpClient();

            Map<String, Object> params= new HashMap<String, Object>(1);
            params.put( CmdUseSlot.PARAM_ID, p.getId() );
            HttpClient.Response r= c.sendPost(Constants.CMD_USE_SLOT, Utils.encodeParamsAsQueryString(params));

            assertTrue( r.getHttpStatus() == HttpStatus.OK );
            //check operation was performed
            assertTrue( p.getAvailableSlots() ==  availableSlots -1 || p.getAvailableSlots() == 0 );

            CmdResponseSerializer ser= new CmdResponseSerializer();
            CmdResponse cr= ser.decode( r.getBufferedReader() );

            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE );

        }finally {
            server.stop();
        }
    }

    @Test
    public void whenIdIsNotFoundAppErrorIsReturned() throws Exception {
        Server server= new Server();
        server.start();

        //No data inserted

        try {
            HttpClient c = new HttpClient();

            Map<String, Object> params= new HashMap<String, Object>(1);
            params.put( CmdUseSlot.PARAM_ID, 1 );

            HttpClient.Response r= c.sendPost(Constants.CMD_FREE_SLOT, Utils.encodeParamsAsQueryString(params));

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            CmdResponseSerializer ser= new CmdResponseSerializer();
            CmdResponse cr= ser.decode( r.getBufferedReader() );

            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );

        }finally {
            server.stop();
        }
    }
}