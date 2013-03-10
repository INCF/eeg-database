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
import cz.zcu.kiv.eegdatabase.wui.ui.account.ChangePasswordPage;
import cz.zcu.kiv.eegdatabase.wui.ui.account.SocialNetworksPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.MyGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ForgottenPasswordPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.RegistrationPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

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

        //Enables encryption of generated URLs
        //Pages mounted in mountPages method remain bookmarkable and accessible by their mount name
        setRootRequestMapper(new CryptoMapper(getRootRequestMapperAsCompound(), this));

        // mount pages in wicket application for better working with pages.
        mountPages();


    }

    private void mountPages() {

        mountPage("welcome", WelcomePage.class);

        mountPage("access-denied", AccessDeniedPage.class);
        //mountPage("registration-new", RegistrationPage.class);
        mountPage("registration-confirm", ConfirmPage.class);
        //mountPage("forgotten-pass", ForgottenPasswordPage.class);
        mountPage("under-construct", UnderConstructPage.class);

        mountPage("account-overview", AccountOverViewPage.class);
        //mountPage("account-change-pass", ChangePasswordPage.class);
        mountPage("account-social", SocialNetworksPage.class);

        mountPage("articles-page", ArticlesPage.class);

        mountPage("experiments-list", ListExperimentsPage.class);
        mountPage("experiments-detail", ExperimentsDetailPage.class);
        mountPage("file-detail", DataFileDetailPage.class);

        mountPage("groups-list", ListResearchGroupsPage.class);
        mountPage("groups-detail", ResearchGroupsDetailPage.class);
        mountPage("groups-my", MyGroupsPage.class);

        mountPage("people-list", ListPersonPage.class);
        mountPage("people-detail", PersonDetailPage.class);

        mountPage("history-page", HistoryPage.class);
        mountPage("home-page", HomePage.class);
        mountPage("lists-page", ListsPage.class);
        mountPage("scenarios-page", ListScenariosPage.class);
        mountPage("search-page", SearchPage.class);

    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = (ConverterLocator) super.newConverterLocator();

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
