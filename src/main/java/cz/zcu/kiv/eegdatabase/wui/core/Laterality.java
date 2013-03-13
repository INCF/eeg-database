package cz.zcu.kiv.eegdatabase.wui.core;

import java.util.ArrayList;
import java.util.List;

public enum Laterality {

    LEFT('L'),
    RIGHT('R'),
    UNSPECIFIED('X');

    public static final String ENUM_PROPERTY_PREFIX = "general.gender.${name}";

    private char shortcut;

    private Laterality(char shortcut) {
        this.shortcut = shortcut;
    }

    public char getShortcut() {
        return shortcut;
    }

    public static Laterality getLateralityByShortcut(char shortcut) {

        for (Laterality tmp : values()) {
            if (tmp.getShortcut() == shortcut)
                return tmp;
        }
        return null;
    }

    public static List<Character> getShortcutList() {
        
        List<Character> list = new ArrayList<Character>();
        for (Laterality tmp : values()) {
            list.add(tmp.getShortcut());
        }
        return list;
    }
}
