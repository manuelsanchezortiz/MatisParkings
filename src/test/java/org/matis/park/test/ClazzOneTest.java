package org.matis.park.test;

import org.junit.Before;
import org.junit.Test;
import org.matis.park.ClazzOne;

import static org.junit.Assert.*;

public class ClazzOneTest {

    private ClazzOne clazz;

    @Before
    public void init(){
        this.clazz= new ClazzOne();
    }

    @Test(expected = RuntimeException.class)
    public final void whenNoArgumentsAddsThenExceptionIsThrown(){
        this.clazz.add(null);
    }

    @Test(expected = RuntimeException.class)
    public final void whenEmptyArgumentsAddsThenExceptionIsThrown(){
        this.clazz.add("");
    }
}