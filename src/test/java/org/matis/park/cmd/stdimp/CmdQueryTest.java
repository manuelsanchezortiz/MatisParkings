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

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class CmdQueryTest {

    private Collection<IParking> getParkings(){

        Collection<IParking> r= new ArrayList<IParking>(5);

        IParking p= new Parking();
        p.setId(1);
        p.setName("Park01");
        p.setTotalSlots(1000);
        p.setAvailableSlots(560);
        p.setOpeningHour(9);
        p.setClosingHour(18);
        p.setGpsLat(41.3850639f);
        p.setGpsLong(2.17340349f);
        p.setOpeningDays(new HashSet<Integer>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY )));
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


        return r;
    }

    @Test
    public void whenQueryingAllReturnAll() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent
        for( IParking p: this.getParkings() ){
            server.getParkingDao().insert(p);
        }

        //query data
        try {
            HttpClient c = new HttpClient();

            HttpClient.Response r= c.sendGet(Constants.CMD_QUERY, null, new HttpClient.DataReader() {
                @Override
                public void read(InputStream is) {



                }
            });

            final TextParkingDto dto= new TextParkingDto();

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            ICmd cmdAdd= server.getCmdRegistry().getCmd( Constants.CMD_QUERY );
            CmdResponse cr= cmdAdd.parseResponse( r.getResponse() );
            assertTrue( cr != null );

            //All data fits in page size, or not

            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE );

            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE_PARTIAL );



/*

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
*/

        }finally {
            server.stop();
        }
    }
}