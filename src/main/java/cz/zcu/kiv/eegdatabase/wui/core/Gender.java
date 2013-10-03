/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   Gender.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
