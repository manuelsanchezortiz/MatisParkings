package org.matis.park.server;

import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.stdimp.Constants;
import org.matis.park.model.Parking;

import java.util.*;

/**
 * Created by manuel on 5/11/14.
 * <p>This class starts the server with sample data, see method {@link #main(String[])} for arguments</p>
 */
public class Service {

    /**
     * Sample data
     * @return some initial data preloaded into the server this service class publishes
     */
    private static Collection<Parking> getParkings(){

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

    /**
     *
     * @param args, only one the port to use for listening for requests
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Server server= new Server();

        //Easy no http request, tests are better if they are independent
        for( Parking p: getParkings() ){
            server.getParkingDao().insert(p);
        }

        if( args.length >= 1 ){

            try {
                int port = Integer.parseInt(args[0]);
                server.start(port);

            }catch (NumberFormatException ex ){
                System.out.println("Invalid port, aborting!");
                System.err.println("Invalid port, aborting!");
                //undefined error
                System.exit(-1);
            }

        }else {
            server.start();
        }

        System.out.println("Server started!");
        System.out.println("Context: " + Constants.CTX );
        System.out.println("Cmds:");
        for(ICmd cmd: server.getCmdRegistry().getCmds() ){
            System.out.println( "  " + cmd.getCmd() +  ", Version: " + cmd.getVersion() );
        }
        System.out.println("Sample use: 'curl --header \"CMD_VERSION:001\" http://localhost:8080/parksvc/query?count=2");
    }
}
