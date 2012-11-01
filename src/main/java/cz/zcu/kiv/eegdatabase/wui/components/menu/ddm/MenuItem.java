package cz.zcu.kiv.eegdatabase.wui.components.menu.ddm;

import java.util.ArrayList;
import java.util.List;

import cz.zcu.kiv.eegdatabase.wui.components.menu.MenuDefinition;

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
