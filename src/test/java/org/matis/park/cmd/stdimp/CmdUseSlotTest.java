package org.matis.park.cmd.stdimp;

import org.junit.Test;
import org.matis.park.Constants;
import org.matis.park.Server;
import org.matis.park.cmd.ICmd;
import org.matis.park.modelobj.IParking;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.HttpClient;
import org.matis.park.util.HttpStatus;
import org.matis.park.util.TestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class CmdUseSlotTest {

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
    public void whenUsingSlotAvailabilityIsReduced() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent
        final IParking p= this.getAParking();
        server.getParkingDao().insert( p );

        int availableSlots= p.getAvailableSlots();

        try {
            HttpClient c = new HttpClient();

            HttpClient.Response r= c.sendPost(Constants.CMD_USE_SLOT, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {

                    Map<String, Object> params= new HashMap<String, Object>(1);
                    params.put( CmdUseSlot.PARAM_ID, p.getId() );

                    try {
                        TestUtils.encodeParamsAsQueryString(params, os);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            assertTrue( p.getAvailableSlots() ==  availableSlots -1 || p.getAvailableSlots() == 0 );

            //Check cmd response, has to be duplicated
            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_USE_SLOT );
            CmdResponse cr= cmdAdd.parseResponse( r.getResponse() );
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

            HttpClient.Response r= c.sendPost(Constants.CMD_FREE_SLOT, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {

                    Map<String, Object> params= new HashMap<String, Object>(1);
                    params.put( CmdUseSlot.PARAM_ID, 1 );

                    try {
                        TestUtils.encodeParamsAsQueryString(params, os);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //Check cmd response, has to be duplicated
            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_FREE_SLOT );
            CmdResponse cr= cmdAdd.parseResponse( r.getResponse() );
            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );

        }finally {
            server.stop();
        }
    }
}