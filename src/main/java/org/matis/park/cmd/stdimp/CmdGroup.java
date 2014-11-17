package org.matis.park.cmd.stdimp;

import org.matis.park.cmd.ICmd;
import org.matis.park.cmd.ICmdGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by manuel on 17/11/14.
 * <p>Implements the standard set of commands</p>
 */
public class CmdGroup implements ICmdGroup{

    @Override
    public Collection<ICmd> getCommands() {

        List<ICmd> r= new ArrayList<ICmd>(5);

        r.add(new CmdAdd());
        r.add(new CmdUpdate());
        r.add(new CmdQuery());
        r.add(new CmdFreeSlot());
        r.add(new CmdUseSlot());

        return r;
    }
}
