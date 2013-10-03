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
 *   EEGDatabaseMainMenu.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.app.menu;

import cz.zcu.kiv.eegdatabase.wui.components.menu.MenuDefinition;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.ChangeUserRolePage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsByPackagePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;

/**
 * GenericMainMenu - add menu page like value of enum.
 * 
 * @author Jakub Rinkes
 * 
 */
public enum EEGDatabaseMainMenu implements MenuDefinition {

    HomePage(HomePage.class, "menuItem.home"),
    ArticlesPage(ArticlesPage.class, "menuItem.articles"),
    SearchPage(SearchPage.class, "title.page.search.menu"),
    ExperimentsPage(ListExperimentsByPackagePage.class, "menuItem.experiments"),
    ScenariosPage(ListScenariosPage.class, "menuItem.scenarios"),
    GroupsPage(ListResearchGroupsPage.class, "menuItem.groups"),
    PeoplePage(ListPersonPage.class, "menuItem.people"),
    ListsPage(ListListsPage.class, "menuItem.lists"),
    HistoryPage(HistoryPage.class, "menuItem.history"),
    Administration(ChangeUserRolePage.class, "menuItem.administration"),

    Main(new EEGDatabaseMainMenu[] { HomePage, ArticlesPage, SearchPage, ExperimentsPage, ScenariosPage, GroupsPage,
    PeoplePage, ListsPage, HistoryPage, Administration }, ""),

    ;

    private EEGDatabaseMainMenu[] submenu = null;
    private Class<?> page;
    private final String title;

    private EEGDatabaseMainMenu(Class<?> page, String title) {
        this.page = page;
        this.title = title;
    }

    private EEGDatabaseMainMenu(EEGDatabaseMainMenu[] submenu, String title) {
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

    public EEGDatabaseMainMenu[] getSubmenu() {
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
