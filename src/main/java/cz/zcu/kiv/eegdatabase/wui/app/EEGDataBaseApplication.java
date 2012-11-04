package cz.zcu.kiv.eegdatabase.wui.app;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.AccessDeniedPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.GroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PeoplePage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;

public class EEGDataBaseApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

    private ApplicationContext appCtx;

    public java.lang.Class<? extends Page> getHomePage() {

        return HomePage.class;
    };

    @Override
    public void init() {
        super.init();
        
        //getMarkupSettings().setStripWicketTags(true);
        //getMarkupSettings().setCompressWhitespace(true);
        //getMarkupSettings().setStripComments(true);

        // set the security strategy is spring and its this bean
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);
        
        // set access denied page inserted in menu content. 
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        // set true for upload progress.
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
        
        // add spring component injector listener
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        
        // mount pages in wicket application for better working with pages.
        mountPages();

    }

    private void mountPages() {
        
        mountPage("access-denied", AccessDeniedPage.class);
        
        mountPage("articles-page", ArticlesPage.class);
        mountPage("experiments-page", ExperimentsPage.class);
        mountPage("groups-page", GroupsPage.class);
        mountPage("history-page", HistoryPage.class);
        mountPage("home-page", HomePage.class);
        mountPage("lists-page", ListsPage.class);
        mountPage("people-page", PeoplePage.class);
        mountPage("scenarios-page", ScenariosPage.class);
        mountPage("search-page", SearchPage.class);

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
