package cz.zcu.kiv.eegdatabase.wui.components.menu;

import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;

/**
 * GenericMainMenu - add menu page like value of enum.
 * 
 * @author Jakub Rinkes
 * 
 */
public enum GenericMainMenu implements MenuDefinition {

    HomePage(HomePage.class, "title.page.home.menu"),
    ArticlesPage(ArticlesPage.class, "title.page.articles.menu"),
    SearchPage(SearchPage.class, "title.page.search.menu"),
    ExperimentsPage(ListExperimentsPage.class, "title.page.experiments.menu"),
    ScenariosPage(ScenariosPage.class, "title.page.scenarios.menu"),
    GroupsPage(HomePage.class, "title.page.groups.menu"),
    PeoplePage(ListExperimentsPage.class, "title.page.people.menu"),
    ListsPage(ListsPage.class, "title.page.lists.menu"),
    HistoryPage(HistoryPage.class, "title.page.history.menu"),

    Main(new GenericMainMenu[] { HomePage, ArticlesPage, SearchPage, ExperimentsPage, ScenariosPage, GroupsPage,
    PeoplePage, ListsPage, HistoryPage }, ""),

    ;

    private GenericMainMenu[] submenu = null;
    private Class<?> page;
    private final String title;

    private GenericMainMenu(Class<?> page, String title) {
        this.page = page;
        this.title = title;
    }

    private GenericMainMenu(GenericMainMenu[] submenu, String title) {
        this.submenu = submenu;
        this.title = title;
    }

    @Override
    public Class getPage() {
        return page;
    }

    public String getTitle() {
        return title;
    }

    public GenericMainMenu[] getSubmenu() {
        return submenu;
    }

    @Override
    public boolean isLinkable() {
        return page != null;
    }

    @Override
    public boolean hasSubmenu() {
        return submenu != null;
    }
}
