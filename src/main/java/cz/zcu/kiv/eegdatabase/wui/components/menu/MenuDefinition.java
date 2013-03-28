package cz.zcu.kiv.eegdatabase.wui.components.menu;

/**
 * Interface for creating menu component. Works with MainMenu component. 
 * 
 * @author Jakub Rinkes
 *
 */
public interface MenuDefinition {

    boolean isLinkable();

    boolean hasSubmenu();

    String getTitle();

    MenuDefinition[] getSubmenu();

    Class<?> getPage();
}
