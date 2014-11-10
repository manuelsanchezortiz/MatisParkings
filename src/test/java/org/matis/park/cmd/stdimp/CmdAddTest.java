package org.matis.park.cmd.stdimp;


import org.junit.Test;
import org.matis.park.Constants;
import org.matis.park.Server;
import org.matis.park.cmd.ICmd;
import org.matis.park.dto.TextParkingDto;
import org.matis.park.modelobj.IParking;
import org.matis.park.modelobj.Parking;
import org.matis.park.util.HttpClient;
import org.matis.park.util.HttpStatus;

import java.io.OutputStream;

import static org.junit.Assert.*;

public class CmdAddTest {

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
    public void whenAddingAParkingWeReceiveAProtocolOKTest() throws Exception{

        Server server= new Server();
        server.start();

        try {
            HttpClient c = new HttpClient();

            final IParking p= this.getAParking();

            final TextParkingDto dto= new TextParkingDto();

            HttpClient.Response r= c.sendPost(Constants.CMD_ADD, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {
                    dto.encode(p, os);
                }
            });

            assertTrue(r.getHttpStatus() == HttpStatus.OK);

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

            final IParking p= this.getAParking();

            final TextParkingDto dto= new TextParkingDto();

            HttpClient.Response r= c.sendPost(Constants.CMD_ADD, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {
                    dto.encode(p, os);
                }
            });

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //may need to check validity of a parking, but not in this test

            r= c.sendPost(Constants.CMD_ADD, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {
                    dto.encode(p, os);
                }
            });

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //Check cmd response, has to be duplicated
            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_ADD );
            CmdResponse cr= cmdAdd.parseResponse( r.getResponse() );
            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.DUPLICATED );

        }finally {
            server.stop();
        }
    }
}