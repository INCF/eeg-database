package cz.zcu.kiv.eegdatabase.wui.ui.home;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class HomePage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    public HomePage() {

        add(new HomeLoginForm("login"));
        setPageTitle(ResourceUtils.getModel("title.page.home"));
    }

}
