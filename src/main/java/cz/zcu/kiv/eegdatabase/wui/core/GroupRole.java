package cz.zcu.kiv.eegdatabase.wui.core;

import java.util.ArrayList;
import java.util.List;

public enum GroupRole {

    GROUP_READER,
    GROUP_EXPERIMENTER,
    GROUP_ADMIN, ;

    public static List<String> getListOfNames() {

        List<String> list = new ArrayList<String>();

        for (GroupRole role : values())
            list.add(role.name());

        return list;
    }

}