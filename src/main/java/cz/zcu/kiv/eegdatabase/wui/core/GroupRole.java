package cz.zcu.kiv.eegdatabase.wui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration for group role values.
 * 
 * @author Jakub Rinkes
 *
 */
public enum GroupRole {

    GROUP_READER,
    GROUP_EXPERIMENTER,
    GROUP_ADMIN, ;
    
    /**
     * Method create list of names.
     * 
     * @return
     */
    public static List<String> getListOfNames() {

        List<String> list = new ArrayList<String>();

        for (GroupRole role : values())
            list.add(role.name());

        return list;
    }

}