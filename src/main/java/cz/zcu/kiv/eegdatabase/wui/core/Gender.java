package cz.zcu.kiv.eegdatabase.wui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration for gender.
 * 
 * @author Jakub Rinkes
 *
 */
public enum Gender {

    MALE('M'),
    FEMALE('F');

    public static final String ENUM_PROPERTY_PREFIX = "general.gender.${name}";

    private char shortcut;

    private Gender(char shortcut) {
        this.shortcut = shortcut;
    }

    public char getShortcut() {
        return shortcut;
    }
    
    /**
     * Parse database value to enum value.
     * 
     * @param shortcut
     * @return
     */
    public static Gender getGenderByShortcut(char shortcut) {

        for (Gender tmp : values()) {
            if (tmp.getShortcut() == shortcut)
                return tmp;
        }
        return null;
    }
    
    /**
     * Method create list of shortcuts.
     * 
     * @return
     */
    public static List<Character> getShortcutList() {

        List<Character> list = new ArrayList<Character>();
        for (Gender tmp : values()) {
            list.add(tmp.getShortcut());
        }
        return list;
    }

}
