package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 15:45
 */
public class WizardTabbedPanelPage extends MenuPage {
    public WizardTabbedPanelPage() {

        // create a list of ITab objects used to feed the tabbed panel
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new Model<String>("Scenario"))
        {
            @Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel1(panelId);
            }
        });

        tabs.add(new AbstractTab(new Model<String>("Environment"))
        {
            @Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel2(panelId);
            }
        });

        tabs.add(new AbstractTab(new Model<String>("Result"))
        {
            @Override
            public Panel getPanel(String panelId)
            {
                return new TabPanel3(panelId);
            }
        });

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        add(new AjaxTabbedPanel("steps", tabs));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    /**
     * Panel representing the content panel for the first tab.
     */
    private class TabPanel1 extends Panel
    {
        /**
         * Constructor
         *
         * @param id
         *            component id
         */
        public TabPanel1(String id)
        {
            super(id);
            add(new AddExperimentScenarioForm("scenarioTab"));
        }
    };

    /**
     * Panel representing the content panel for the second tab.
     */
    private static class TabPanel2 extends Panel
    {
        /**
         * Constructor
         *
         * @param id
         *            component id
         */
        public TabPanel2(String id)
        {
            super(id);
            add(new AddExperimentEnvironmentForm("environmentTab"));
        }
    };

    /**
     * Panel representing the content panel for the third tab.
     */
    private static class TabPanel3 extends Panel
    {
        /**
         * Constructor
         *
         * @param id
         *            component id
         */
        public TabPanel3(String id)
        {
            super(id);
            add(new AddExperimentResultsForm("resultTab"));
        }
    };
}
