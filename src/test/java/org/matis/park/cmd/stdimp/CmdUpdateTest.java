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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CmdUpdateTest {
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
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY )));

        return p;
    }

    @Test
    public void whenUpdatingParkingIsReallyModified() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent

        //this will be modified from the http request
        IParking pStored= this.getAParking();
        server.getParkingDao().insert( pStored );

        final IParking pUsedToModifyWithHttpRequest= this.getAParking();
        //alter p. id must remain equals as per spec

        pUsedToModifyWithHttpRequest.setName("Park02");
        pUsedToModifyWithHttpRequest.setTotalSlots(120);
        pUsedToModifyWithHttpRequest.setAvailableSlots(5);
        pUsedToModifyWithHttpRequest.setOpeningHour(7);
        pUsedToModifyWithHttpRequest.setClosingHour(20);
        pUsedToModifyWithHttpRequest.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.THURSDAY)));

        final TextParkingDto dto= new TextParkingDto();


        //update via http
        try {
            HttpClient c = new HttpClient();

            HttpClient.Response r= c.sendPost(Constants.CMD_UPDATE, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {
                    //send the one with modifications
                    dto.encode(pUsedToModifyWithHttpRequest, os);
                }
            });

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //update the stored object
            pStored= server.getParkingDao().getParkingWithId(pStored.getId());

            //now if stored one has been changed to be equal to the modified one used in the http request
            assertEquals(pStored, pUsedToModifyWithHttpRequest  );

            //Check cmd response, has to be duplicated
            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_UPDATE );
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

        //Easy no http request, tests are better if they are independent

        //this will be modified from the http request
        IParking pStored= this.getAParking();
        server.getParkingDao().insert( pStored );

        final IParking pUsedToModifyWithHttpRequest= this.getAParking();
        //alter p. id must remain equals as per spec
        pUsedToModifyWithHttpRequest.setId(2);
        pUsedToModifyWithHttpRequest.setName("Park02");
        pUsedToModifyWithHttpRequest.setTotalSlots(120);
        pUsedToModifyWithHttpRequest.setAvailableSlots(5);
        pUsedToModifyWithHttpRequest.setOpeningHour(7);
        pUsedToModifyWithHttpRequest.setClosingHour(20);
        pUsedToModifyWithHttpRequest.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.THURSDAY)));

        final TextParkingDto dto= new TextParkingDto();


        //update via http
        try {
            HttpClient c = new HttpClient();

            HttpClient.Response r= c.sendPost(Constants.CMD_UPDATE, new HttpClient.DataWriter() {
                @Override
                public void write(OutputStream os) {
                    //send the one with modifications
                    dto.encode(pUsedToModifyWithHttpRequest, os);
                }
            });

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            //Check cmd response, has to be duplicated
            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_UPDATE );
            CmdResponse cr= cmdAdd.parseResponse( r.getResponse() );
            assertTrue( cr != null );
            assertTrue( cr.getAppCode() == CmdErrorCodes.ENTITY_NOT_FOUND );

        }finally {
            server.stop();
        }
    }
}