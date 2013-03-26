package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 25.3.13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentPage extends MenuPage {
    public AddExperimentPage(PageParameters parameter){
        setupComponents();
    }

    private void setupComponents() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        add(new AddExperimentScenarioForm("scenarioTab"));
        add(new AddExperimentEnvironmentForm("environmentTab"));
        add(new AddExperimentResultsForm("resultTab"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/jquery-1.9.1.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/jquery-ui-1.10.1.custom.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/jquery.validate.min.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/date.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/experiments/wizard.js"));
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }
}
