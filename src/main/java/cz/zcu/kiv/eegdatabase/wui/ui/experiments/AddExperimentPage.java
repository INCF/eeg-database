package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 25.3.13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentPage extends MenuPage {

    public AddExperimentPage(PageParameters parameters) {

        setupComponents();
    }

    private void setupComponents() {
        setPageTitle(ResourceUtils.getModel("pageTitle.addExperimentPage"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        add(new AddExperimentForm("addExperiment"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/wizard.js"));
        super.renderHead(response);
    }
}
