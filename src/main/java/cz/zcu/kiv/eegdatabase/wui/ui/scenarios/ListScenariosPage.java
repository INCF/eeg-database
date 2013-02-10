package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.StyleClassPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

@AuthorizeInstantiation("ROLE_USER")
public class ListScenariosPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    ScenariosFacade scenariosFacade;

    public ListScenariosPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfScenarios"));
        setupComponents();
    }

    private void setupComponents() {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        DefaultDataTable<Scenario, String> list = new DefaultDataTable<Scenario, String>("list", createListColumns(),
                new ListScenariosDataProvider(scenariosFacade), ITEMS_PER_PAGE);

        add(list);

    }

    private List<? extends IColumn<Scenario, String>> createListColumns() {
        List<IColumn<Scenario, String>> columns = new ArrayList<IColumn<Scenario, String>>();

        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.id"), "scenarioId", "scenarioId", "width30"));
        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.title"), "title", "title", "width300"));
        columns.add(new PropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.scenarioLength"), "scenarioLength", "scenarioLength"));

        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null, "width80") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Scenario>> item, String componentId, IModel<Scenario> rowModel) {
                item.add(new ViewLinkPanel(componentId, WelcomePage.class, "scenarioId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        // TODO download file if he exist
        columns.add(new StyleClassPropertyColumn<Scenario, String>(ResourceUtils.getModel("dataTable.heading.file"), null, null, "width80"));

        return columns;
    }
}
