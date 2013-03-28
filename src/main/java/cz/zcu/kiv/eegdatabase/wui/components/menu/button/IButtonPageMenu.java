package cz.zcu.kiv.eegdatabase.wui.components.menu.button;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

/**
 * Interface for LeftMenuStructure for ButtonPageMenu component.
 * 
 * @author Jakub Rinkes
 * 
 */
public interface IButtonPageMenu {

    Class<? extends MenuPage> getPageClass();

    String getPageTitleKey();

    PageParameters getPageParameters();
}
