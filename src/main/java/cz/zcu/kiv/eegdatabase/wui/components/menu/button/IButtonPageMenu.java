package cz.zcu.kiv.eegdatabase.wui.components.menu.button;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

public interface IButtonPageMenu {
    
    Class<? extends MenuPage> getPageClass();
    String getPageTitleKey();
}
