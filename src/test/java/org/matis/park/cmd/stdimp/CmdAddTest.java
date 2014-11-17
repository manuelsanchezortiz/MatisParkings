package org.matis.park.cmd.stdimp;


import org.junit.Test;
import org.matis.park.server.Server;
import org.matis.park.dto.CmdResponseSerializer;
import org.matis.park.dto.TransferUtil;
import org.matis.park.model.Parking;
import org.matis.park.server.util.HttpClient;
import org.matis.park.server.util.HttpStatus;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class CmdAddTest {

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
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)));

        return p;
    }

    @Test
    public void whenAddingAParkingWeReceiveAProtocolOKTest() throws Exception{

        Server server= new Server();
        server.start();

        try {
            HttpClient c = new HttpClient();

            final Parking p= this.getAParking();

            String s= TransferUtil.parkingToString(p);

            HttpClient.Response r= c.sendPost(Constants.CMD_ADD, s);

            CmdResponseSerializer rs= new CmdResponseSerializer();
            CmdResponse cr= rs.decode( r.getBufferedReader() );

            assertTrue(r.getHttpStatus() == HttpStatus.OK);
            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE );

        }finally {
            server.stop();
        }
    }

    @Test
    public void whenAddingAParkingTwiceWeReceiveDuplicateErrorTest() throws Exception{
        Server server= new Server();
        server.start();

        try {
            HttpClient c = new HttpClient();

            final Parking p= this.getAParking();
            String s= TransferUtil.parkingToString(p);

            HttpClient.Response r= c.sendPost(Constants.CMD_ADD, s);
            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //may need to check response, but not in this test

            r= c.sendPost(Constants.CMD_ADD, s );
            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //Check cmd response, has to be duplicated
            CmdResponseSerializer ser= new CmdResponseSerializer();
            CmdResponse cr= ser.decode( r.getBufferedReader() );

            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.DUPLICATED );

        }finally {
            server.stop();
        }
    }
}