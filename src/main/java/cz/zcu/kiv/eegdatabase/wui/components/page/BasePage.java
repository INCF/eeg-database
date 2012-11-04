package cz.zcu.kiv.eegdatabase.wui.components.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class BasePage extends WebPage {

    private static final long serialVersionUID = 5654545812586020460L;

    public static final String DEFAULT_PARAM_ID = "DEFAULT_PARAM_ID";

    public BasePage() {
        add(new Label("pageTitle", ResourceUtils.getModel("title.app")));
    }

    protected void setPageTitle(IModel<String> model) {
        get("pageTitle").setDefaultModel(model);
    }
}
