package org.matis.park.cmd.stdimp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HaversineTest {

    @Test
    public void testHaversine() throws Exception {

        double d= Haversine.haversine(36.12, -86.67, 33.94, -118.40);

        assertEquals( d , 2887259, 10);
    }
}