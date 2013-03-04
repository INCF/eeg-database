package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListExperimentsPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int ITEMS_PER_PAGE = 20;
    public static final String PARAM_OWNER = "OWNER";
    public static final String PARAM_SUBJECT = "SUBJECT";

    @SpringBean
    ExperimentsFacade facade;

    @SpringBean
    ResearchGroupFacade researchFacade;

    public ListExperimentsPage() {

        setupComponents(null);
    }

    public ListExperimentsPage(PageParameters parameters) {

        String param = parseParameters(parameters);
        setupComponents(param);
    }

    private void setupComponents(String param) {
        boolean owner = false;
        boolean subject = false;

        IModel<String> title = null;
        if (param == null) {
            title = ResourceUtils.getModel("pageTitle.allExperiments");
            add(new Label("title", title));
        } else if (param.equals(PARAM_OWNER)) {
            owner = true;
            title = ResourceUtils.getModel("pageTitle.myExperiments");
            add(new Label("title", title));
        } else if (param.equals(PARAM_SUBJECT)) {
            subject = true;
            title = ResourceUtils.getModel("pageTitle.myExperiments");
            add(new Label("title", title));
        }
        setPageTitle(title);

        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        boolean userNotMemberOfAnyGroup = researchFacade.getResearchGroupsWhereMember(loggedUser).isEmpty();
        add(new Label("userNotMemberOfAnyGroup", ResourceUtils.getModel("text.notMemberOfAnyGroup")).setVisibilityAllowed(userNotMemberOfAnyGroup));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        DefaultDataTable<Experiment, String> list = new DefaultDataTable<Experiment, String>("list", createListColumns(),
                new ListExperimentsDataProvider(facade, loggedUser, owner, subject), ITEMS_PER_PAGE);

        add(list);
    }

    private String parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toString();
    }

    private List<? extends IColumn<Experiment, String>> createListColumns() {
        List<IColumn<Experiment, String>> columns = new ArrayList<IColumn<Experiment, String>>();

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.number"), "experimentId", "experimentId"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.scenarioTitle"), "scenario.title", "scenario.title"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.date"), "startTime", "startTime"));
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.gender"), "personBySubjectPersonId.gender", "personBySubjectPersonId.gender"));
        columns.add(new TimestampPropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.yearOfBirth"), "personBySubjectPersonId.dateOfBirth",
                "personBySubjectPersonId.dateOfBirth", "yyyy"));
        // TODO service page missing.
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.services"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        // TODO download experiment missing.
        columns.add(new PropertyColumn<Experiment, String>(ResourceUtils.getModel("dataTable.heading.download"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<Experiment>> item, String componentId, IModel<Experiment> rowModel) {
                item.add(new ViewLinkPanel(componentId, ExperimentsDetailPage.class, "experimentId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
    }
}
