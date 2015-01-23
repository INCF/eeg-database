/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   EEGDataBaseApplication.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.app;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.core.request.mapper.CryptoMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.AccessDeniedPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.InternalErrorPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.core.common.DiseaseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.AccountOverViewPage;
import cz.zcu.kiv.eegdatabase.wui.ui.account.SocialNetworksPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManagePersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageUserRolePage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.ManageResearchGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticleCommentFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticleFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesSettingsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ViewArticlePage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.AddDataFilePage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentOptParamValueFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDownloadPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPackageDownloadPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ManageExperimentPackagesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.DiseaseConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.HardwareConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.PersonConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.PharmaceuticalConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.ProjectTypeConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.ResearchGroupConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.ScenarioConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.SoftwareConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.StimulusConverter;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.WeatherConverter;
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
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.GrantedLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.LicenseRequestPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.ManageLicenseRequestsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.RevokedRequestPage;
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
import cz.zcu.kiv.eegdatabase.wui.ui.order.ListOrderPage;
import cz.zcu.kiv.eegdatabase.wui.ui.order.OrderDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonAddParamFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.pricing.PriceListPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form.ScenarioFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form.ScenarioSchemaFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

/**
 * Main class for wicket core. Initialization of wicket core, mounter pages on specific url, prepare project settings: security policy, redirect policy.
 * 
 * @author Jakub Rinkes
 * 
 */
public class EEGDataBaseApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

    private ApplicationContext appCtx;
    
    @Value("${app.domain}")
    private String domain;

    @Autowired
    private DiseaseFacade diseaseFacade;
    @Autowired
    private PersonFacade personFacade;
    @Autowired
    private PharmaceuticalFacade pharmaceuticalFacade;
    @Autowired
    private ProjectTypeFacade projectTypeFacade;
    @Autowired
    private ResearchGroupFacade researchGroupFacade;
    @Autowired
    private ScenariosFacade scenariosFacade;
    @Autowired
    private HardwareFacade hardwareFacade;
    @Autowired
    private SoftwareFacade softwareFacade;
    @Autowired
    private WeatherFacade weatherFacade;
    @Autowired
    private StimulusFacade stimulusFacade;

    private boolean development = true;

    public java.lang.Class<? extends Page> getHomePage() {

        return HomePage.class;
    };
    
    public static EEGDataBaseApplication get()
    {
        return (EEGDataBaseApplication) AuthenticatedWebApplication.get();
    }

    @Override
    public void init() {
        super.init();

        getDebugSettings().setOutputComponentPath(development);
        getMarkupSettings().setStripWicketTags(true);
        // getMarkupSettings().setCompressWhitespace(true);
        getMarkupSettings().setStripComments(true);

        // set the security strategy is spring and its this bean
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);

        // set access denied page inserted in menu content.
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
        
        if(!development){
            
            getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
        }
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
    
    @Override
    public RuntimeConfigurationType getConfigurationType() {
        
        return development ? RuntimeConfigurationType.DEVELOPMENT : RuntimeConfigurationType.DEPLOYMENT;
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
        mountPage("internal-error", InternalErrorPage.class);

        mountPage("account-overview", AccountOverViewPage.class);
        // mountPage("account-change-pass", ChangePasswordPage.class);
        mountPage("account-social", SocialNetworksPage.class);

        mountPage("administration-manage-user-role", AdminManageUserRolePage.class);
        mountPage("administration-manage-user", AdminManagePersonPage.class);
        mountPage("administration-manage-group", ManageResearchGroupPage.class);

        mountPage("articles-list", ArticlesPage.class);
        mountPage("articles-form", ArticleFormPage.class);
        mountPage("articles-view", ViewArticlePage.class);
        mountPage("articles-comment-add", ArticleCommentFormPage.class);
        mountPage("articles-settings", ArticlesSettingsPage.class);

        mountPage("experiments-list", ListExperimentsPage.class);
        mountPage("experiments-detail", ExperimentsDetailPage.class);
        mountPage("experiments-form", ExperimentFormPage.class);
        mountPage("experiments-download", ExperimentsDownloadPage.class);
        mountPage("experiments-package-download", ExperimentsPackageDownloadPage.class);
        mountPage("experiments-add-file", AddDataFilePage.class);
        mountPage("experiments-add-param", ExperimentOptParamValueFormPage.class);
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
        mountPage("home-page", HomePage.class);
        mountPage("scenarios-page", ListScenariosPage.class);
        mountPage("scenarios-detail", ScenarioDetailPage.class);
        mountPage("scenarios-form", ScenarioFormPage.class);
        mountPage("scenarios-schema-form", ScenarioSchemaFormPage.class);
        mountPage("search-page", SearchPage.class);
        mountPage("add-experiment-wizard-page", ExperimentFormPage.class);

        mountPage("manage-packages", ManageExperimentPackagesPage.class);
        mountPage("license-request", LicenseRequestPage.class);
        mountPage("granted-licenses", GrantedLicensesPage.class);
        mountPage("manage-license-requests", ManageLicenseRequestsPage.class);
        mountPage("revoked-licenses", RevokedRequestPage.class);
        
        mountPackage("order-list", ListOrderPage.class);
        mountPackage("order-view", OrderDetailPage.class);
        
// XXX price list hidden for now.
//        mountPage("pricelist", PriceListPage.class);

        mountPage("elastic", Elastic.class);
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = (ConverterLocator) super.newConverterLocator();

        // here should be added custom convertor for converting types.

        locator.set(Disease.class, new DiseaseConverter(diseaseFacade));
        locator.set(Person.class, new PersonConverter(personFacade));
        locator.set(Pharmaceutical.class, new PharmaceuticalConverter(pharmaceuticalFacade));
        locator.set(ProjectType.class, new ProjectTypeConverter(projectTypeFacade));
        locator.set(ResearchGroup.class, new ResearchGroupConverter(researchGroupFacade));
        locator.set(Scenario.class, new ScenarioConverter(scenariosFacade));
        locator.set(Hardware.class, new HardwareConverter(hardwareFacade));
        locator.set(Software.class, new SoftwareConverter(softwareFacade));
        locator.set(Weather.class, new WeatherConverter(weatherFacade));
        locator.set(Stimulus.class, new StimulusConverter(stimulusFacade));

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

    public void setDevelopment(boolean development) {
        this.development = development;
    }
    
    public String getDomain() {
        return domain;
    }

}
