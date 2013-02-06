package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.canvas.ExperimentSignalViewCanvasPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.security.Gender;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

@AuthorizeInstantiation("ROLE_USER")
public class ExperimentsDetailPage extends MenuPage {

    private static final long serialVersionUID = 957980612639804114L;

    @SpringBean
    ExperimentsFacade facade;

    public ExperimentsDetailPage() {
        setupComponents(null);
    }

    public ExperimentsDetailPage(PageParameters parameters) {

        Long experimentId = parseParameters(parameters);

        setupComponents(experimentId);
    }

    private void setupComponents(Long experimentId) {
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        Experiment experiment = facade.getExperimentForDetail(experimentId.intValue());

        add(new Label("experimentId", experiment.getExperimentId()));
        add(new TimestampLabel("startTime", experiment.getStartTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new TimestampLabel("endTime", experiment.getEndTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new Label("temperature", experiment.getTemperature()));
        add(new Label("weather.title", experiment.getWeather().getTitle()));
        add(new Label("environmentNote", experiment.getEnvironmentNote()));
        add(new Label("privateExperiment", experiment.isPrivateExperiment()));
        add(new TimestampLabel("dateOfBirth", experiment.getPersonBySubjectPersonId().getDateOfBirth(), StringUtils.DATE_TIME_FORMAT_PATTER_ONLY_YEAR));
        add(new EnumLabel<Gender>("gender", Gender.getGenderByShortcut(experiment.getPersonBySubjectPersonId().getGender())));
        // TODO prozatim neni hotova stranka kam tenhle odkaz vede. Dodelat. !! dodat. Scenario Detail
        add(new BookmarkablePageLink("detailLink", WelcomePage.class, PageParametersUtils.getDefaultPageParameters(experiment.getPersonBySubjectPersonId().getPersonId())));

        final ExperimentSignalViewCanvasPanel experimentViewPanel = new ExperimentSignalViewCanvasPanel("view", experiment);
        
        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisibilityAllowed(true);
        container.add(experimentViewPanel);

        add(container);
    }

    private Long parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toLongObject();
    }

}
