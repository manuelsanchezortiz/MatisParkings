package org.matis.park.cmd;

import org.junit.Test;
import org.matis.park.cmd.stdimp.CmdAdd;

public class CmdRegistryTest {

    @Test( expected = CmdRegistry.AlreadyRegisteredException.class)
    public void whenAddingRepeatedCommandExceptionIsThrown() throws Exception {

        CmdRegistry reg= new CmdRegistry();

        ICmd cmd= new CmdAdd();

        reg.addCmd(cmd);

        reg.addCmd(cmd);

    }

}