package cz.zcu.kiv.eegdatabase.wui.components.menu;

public interface MenuDefinition {

    boolean isLinkable();

    boolean hasSubmenu();

    String getTitle();

    MenuDefinition[] getSubmenu();

    Class<?> getPage();
}
