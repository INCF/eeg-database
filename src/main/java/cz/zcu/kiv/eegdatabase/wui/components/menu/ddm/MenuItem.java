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
 *   MenuItem.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.menu.ddm;

import java.util.ArrayList;
import java.util.List;

import cz.zcu.kiv.eegdatabase.wui.components.menu.MenuDefinition;

/**
 * Help object for preprocess menu structure.
 * 
 * @author Jakub Rinkes
 * 
 */
public class MenuItem {

    private MenuDefinition definition;
    private List<MenuItem> submenu = new ArrayList<MenuItem>();

    public MenuItem(MenuDefinition definition) {

        this.definition = definition;

        if (definition.hasSubmenu()) {

            for (MenuDefinition tmp : definition.getSubmenu())
                submenu.add(new MenuItem(tmp));
        }
    }

    public MenuItem(MenuDefinition definition, List<MenuItem> submenu) {

        this.definition = definition;
        this.submenu.addAll(submenu);
    }

    public boolean isLinkable() {
        return definition.isLinkable();
    }

    public boolean hasSubmenu() {
        return definition.hasSubmenu();
    }

    public String getString() {
        return definition.getTitle();
    }

    public List<MenuItem> getSubmenu() {
        return submenu;
    }

    public Class getClassForUrl() {
        return definition.getPage();
    }
}
