package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.ArrayList;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.canvas.ExperimentSignalViewCanvasPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentsDetailPage extends MenuPage {

    private static final long serialVersionUID = 957980612639804114L;

    @SpringBean
    ExperimentsFacade facade;

    @SpringBean
    SecurityFacade security;

    public ExperimentsDetailPage(PageParameters parameters) {

        int experimentId = parseParameters(parameters);

        setupComponents(experimentId);
    }

    private void setupComponents(int experimentId) {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        Experiment experiment = facade.getExperimentForDetail(experimentId);

        add(new Label("experimentId", experiment.getExperimentId()+""));
        add(new TimestampLabel("startTime", experiment.getStartTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new TimestampLabel("endTime", experiment.getEndTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new Label("temperature", experiment.getTemperature()+""));
        add(new Label("weather.title", experiment.getWeather().getTitle()));
        add(new Label("environmentNote", experiment.getEnvironmentNote()));
        add(new Label("privateExperiment", experiment.isPrivateExperiment()+""));
        add(new Label("scenario.title", experiment.getScenario().getTitle()));

        Person personBySubjectPersonId = experiment.getPersonBySubjectPersonId();
        add(new TimestampLabel("dateOfBirth", personBySubjectPersonId.getDateOfBirth(), StringUtils.DATE_TIME_FORMAT_PATTER_ONLY_YEAR));
        add(new EnumLabel<Gender>("gender", Gender.getGenderByShortcut(personBySubjectPersonId.getGender())));
        
        BookmarkablePageLink<Void> personLink = new BookmarkablePageLink<Void>("personLink", PersonDetailPage.class, PageParametersUtils.getDefaultPageParameters(personBySubjectPersonId.getPersonId()));
        personLink.setVisibilityAllowed(security.userCanViewPersonDetails(personBySubjectPersonId.getPersonId()));
        add(personLink);
        BookmarkablePageLink<Void> scenarioLink = new BookmarkablePageLink<Void>("scenarioLink", ScenarioDetailPage.class, PageParametersUtils.getDefaultPageParameters(experiment.getScenario().getScenarioId()));
        add(scenarioLink);
        
        // TODO action box pages
        boolean coexperiment = security.userIsOwnerOrCoexperimenter(experimentId);
        BookmarkablePageLink<Void> addParameterLink = new BookmarkablePageLink<Void>("addParameterLink", UnderConstructPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        BookmarkablePageLink<Void> addFileLink = new BookmarkablePageLink<Void>("addFileLink", UnderConstructPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        BookmarkablePageLink<Void> editExpLink = new BookmarkablePageLink<Void>("editExpLink", UnderConstructPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        BookmarkablePageLink<Void> downloadExpLink = new BookmarkablePageLink<Void>("downloadExpLink", ExperimentsDownloadPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        add(addParameterLink.setVisibilityAllowed(coexperiment), addFileLink.setVisibilityAllowed(coexperiment), editExpLink.setVisibilityAllowed(coexperiment), downloadExpLink);

        final ExperimentSignalViewCanvasPanel experimentViewPanel = new ExperimentSignalViewCanvasPanel("view", experiment);

        PropertyListView<Hardware> hardware = new PropertyListView<Hardware>("hardware", new ListModel<Hardware>(new ArrayList<Hardware>(experiment.getHardwares()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Hardware> item) {
                item.add(new Label("title"));
                item.add(new Label("type"));

            }
        };

        PropertyListView<ExperimentOptParamVal> addParameters = new PropertyListView<ExperimentOptParamVal>("addParameters", new ListModel<ExperimentOptParamVal>(new ArrayList<ExperimentOptParamVal>(
                experiment.getExperimentOptParamVals()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ExperimentOptParamVal> item) {
                item.add(new Label("experimentOptParamDef.paramName"));
                item.add(new Label("paramValue"));

            }
        };

        PropertyListView<DataFile> files = new PropertyListView<DataFile>("files", new ListModel<DataFile>(new ArrayList<DataFile>(experiment.getDataFiles()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<DataFile> item) {
                item.add(new Label("filename"));
                item.add(new Label("description"));
                item.add(new ViewLinkPanel("detail", DataFileDetailPage.class, "dataFileId", item.getModel(), ResourceUtils.getModel("link.detail")));

            }
        };

        add(hardware, addParameters, files);

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisibilityAllowed(true);
        container.add(experimentViewPanel);

        add(container);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

}
