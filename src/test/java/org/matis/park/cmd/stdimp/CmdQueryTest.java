package org.matis.park.cmd.stdimp;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.matis.park.dto.CmdQueryResponseSerializer;
import org.matis.park.model.Parking;
import org.matis.park.server.Server;
import org.matis.park.server.util.HttpClient;
import org.matis.park.server.util.HttpStatus;

import java.util.*;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CmdQueryTest {

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

    public void when01QueryingAllReturnAll() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent
        for( Parking p: this.getParkings() ){
            server.getParkingDao().insert(p);
        }

        //query data
        try {
            HttpClient c = new HttpClient();

            HttpClient.Response r= c.sendGet(Constants.CMD_QUERY, null);

            CmdQueryResponseSerializer ser= new CmdQueryResponseSerializer();
            CmdQueryResponse cr= ser.decode( r.getBufferedReader() );

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            assertTrue( cr != null );

            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE );
            assertTrue( cr.getCount() == cr.getParkings().size() );
            assertTrue( cr.getParkings().size() == 5 );


        }finally {
            server.stop();
        }
    }

    @Test
    public void when02QueryingAllOpenReturnTheExpectedList() throws Exception {

        Server server= new Server();
        server.start();

        //Easy no http request, tests are better if they are independent
        for( Parking p: this.getParkings() ){
            server.getParkingDao().insert(p);
        }

        //query data
        try {
            HttpClient c = new HttpClient();

            Map<String,Object> params= new HashMap<String, Object>();
            params.put( SharedConstants.PARAM_COUNT, 2 );
            params.put( SharedConstants.PARAM_OFFSET, 1 );

            HttpClient.Response r= c.sendGet(Constants.CMD_QUERY, params);

            CmdQueryResponseSerializer ser= new CmdQueryResponseSerializer();
            CmdQueryResponse cr= ser.decode( r.getBufferedReader() );

            assertTrue( r.getHttpStatus() == HttpStatus.OK );

            assertTrue( cr != null );

            assertTrue( cr.getAppCode() == CmdErrorCodes.NONE );
            assertTrue( 2 == cr.getParkings().size() );
            assertTrue( cr.getParkings().get(0).getId() == 2 );
            assertTrue( cr.getParkings().get(1).getId() == 3 );

        }finally {
            server.stop();
        }
    }
}