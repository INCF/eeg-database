package cz.zcu.kiv.eegdatabase.wui.app;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.core.request.mapper.CryptoMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.AccessDeniedPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.ui.account.AccountOverViewPage;
import cz.zcu.kiv.eegdatabase.wui.ui.account.SocialNetworksPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.ChangeUserRolePage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDownloadPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListOfMembersGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.MyGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.AddMemberToGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.ResearchGroupFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.role.GroupRoleAcceptPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.role.GroupRoleRequestPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListArtifactDefinitionsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListExperimentOptParamPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListFileMetadataPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListHardwareDefinitionsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListPersonOptParamPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListWeatherDefinitiosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.ArtifactFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.ExperimentOptParamFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.FileMetadataFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.HardwareFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.PersonOptParamFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.form.WeatherFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonAddParamFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form.ScenarioFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form.ScenarioSchemaFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.workflow.WorkflowListResultPage;
import cz.zcu.kiv.eegdatabase.wui.ui.workflow.WorkflowFormPage;

/**
 * Main class for wicket core. Initialization of wicket core, mounter pages on specific url,
 * prepare project settings: security policy, redirect policy.
 * 
 * @author Jakub Rinkes
 *
 */
public class EEGDataBaseApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

    private ApplicationContext appCtx;

    public java.lang.Class<? extends Page> getHomePage() {

        return HomePage.class;
    };

    @Override
    public void init() {
        super.init();

        getMarkupSettings().setStripWicketTags(true);
        // getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripComments(true);

        // set the security strategy is spring and its this bean
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);

        // set access denied page inserted in menu content.
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        // set true for upload progress.
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);

        // add spring component injector listener
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        // Enables encryption of generated URLs
        // Pages mounted in mountPages method remain bookmarkable and accessible by their mount name
        setRootRequestMapper(new CryptoMapper(getRootRequestMapperAsCompound(), this));

        // mount pages in wicket application for better working with pages.
        mountPages();

    }
    
    /**
     * Mount pages on specific URL.
     */
    private void mountPages() {

        mountPage("welcome", WelcomePage.class);

        mountPage("access-denied", AccessDeniedPage.class);
        // mountPage("registration-new", RegistrationPage.class);
        mountPage("registration-confirm", ConfirmPage.class);
        // mountPage("forgotten-pass", ForgottenPasswordPage.class);
        mountPage("under-construct", UnderConstructPage.class);

        mountPage("account-overview", AccountOverViewPage.class);
        // mountPage("account-change-pass", ChangePasswordPage.class);
        mountPage("account-social", SocialNetworksPage.class);
        
        mountPage("administration-change-user-role", ChangeUserRolePage.class);

        mountPage("articles-page", ArticlesPage.class);

        mountPage("experiments-list", ListExperimentsPage.class);
        mountPage("experiments-detail", ExperimentsDetailPage.class);
        mountPage("experiments-download", ExperimentsDownloadPage.class);
        mountPage("file-detail", DataFileDetailPage.class);

        mountPage("groups-list", ListResearchGroupsPage.class);
        mountPage("groups-detail", ResearchGroupsDetailPage.class);
        mountPage("groups-my", MyGroupsPage.class);
        mountPage("groups-member-list", ListOfMembersGroupPage.class);
        mountPage("groups-member-add", AddMemberToGroupPage.class);
        mountPage("groups-form", ResearchGroupFormPage.class);
        mountPage("groups-role-request", GroupRoleRequestPage.class);
        mountPage("groups-role-accept", GroupRoleAcceptPage.class);

        mountPage("people-list", ListPersonPage.class);
        mountPage("people-detail", PersonDetailPage.class);
        mountPage("people-form", PersonFormPage.class);
        mountPage("people-param-form", PersonAddParamFormPage.class);

        mountPage("lists", ListListsPage.class);
        mountPage("lists-weather", ListWeatherDefinitiosPage.class);
        mountPage("lists-weather-form", WeatherFormPage.class);
        mountPage("lists-hardware", ListHardwareDefinitionsPage.class);
        mountPage("lists-hardware-form", HardwareFormPage.class);
        mountPage("lists-file-metadata", ListFileMetadataPage.class);
        mountPage("lists-file-metadata-form", FileMetadataFormPage.class);
        mountPage("lists-person-param", ListPersonOptParamPage.class);
        mountPage("lists-person-param-form", PersonOptParamFormPage.class);
        mountPage("lists-experiment-param", ListExperimentOptParamPage.class);
        mountPage("lists-experiment-param-form", ExperimentOptParamFormPage.class);
        mountPage("lists-artifact", ListArtifactDefinitionsPage.class);
        mountPage("lists-artifact-form", ArtifactFormPage.class);

        mountPage("history-page", HistoryPage.class);
        mountPage("workflow-page-form", WorkflowFormPage.class);
        mountPage("workflow-page-list", WorkflowListResultPage.class);
        mountPage("home-page", HomePage.class);
        mountPage("scenarios-page", ListScenariosPage.class);
        mountPage("scenarios-detail", ScenarioDetailPage.class);
        mountPage("scenarios-form", ScenarioFormPage.class);
        mountPage("scenarios-schema-form", ScenarioSchemaFormPage.class);
        mountPage("search-page", SearchPage.class);

    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = (ConverterLocator) super.newConverterLocator();
        
        // here should be added custom convertor for converting types.
        
        return locator;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new EEGDataBaseSession(request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return EEGDataBaseSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return HomePage.class;
    }

}
