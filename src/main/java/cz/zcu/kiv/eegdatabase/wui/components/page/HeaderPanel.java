/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * HeaderPanel.java, 2. 6. 2015 14:29:08, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.link.LabeledLink;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.account.AccountOverViewPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageUserRolePage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsByPackagePage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.history.HistoryPage;
import cz.zcu.kiv.eegdatabase.wui.ui.lists.ListListsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.search.SearchPage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonList;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

/**
 * @author Jakub Krauz
 *
 */
public class HeaderPanel extends Panel {

    private static final long serialVersionUID = 8718905870967730251L;

    
    public HeaderPanel(String id) {
        super(id);
        
        add(navbar());
        
        /*boolean signedIn = EEGDataBaseSession.get().isSignedIn();

        String labelMessage;
        Class<?> pageClass;
        String labelLink;

        if (signedIn) {
            labelMessage = ResourceUtils.getString("general.header.logged");
            labelMessage += EEGDataBaseSession.get().getLoggedUser().getUsername();
            labelLink = ResourceUtils.getString("general.page.myaccount.link");
            pageClass = AccountOverViewPage.class;
        } else {
            labelMessage = ResourceUtils.getString("general.header.notlogged");
            labelLink = ResourceUtils.getString("action.register");
            pageClass = RegistrationPage.class;
        }

        BookmarkablePageLink headerLink = new BookmarkablePageLink("userHeaderLink", pageClass);
        headerLink.add(new Label("linkLabel", labelLink));
        add(headerLink);


        StringValue searchString = EEGDataBaseSession.get().getSearchString();
        MenuSearchPanel panel = new MenuSearchPanel("menuSearchPanel", searchString);
        add(panel);
        if (!signedIn) {
            panel.setVisible(false);
        }

        add(new Label("userLogStatusLabel", labelMessage));

        add(new MainMenu("mainMenu"));*/
        
    }
    
    
    private Navbar navbar() {
        Navbar navbar = new Navbar("navbar");
        navbar.setPosition(Navbar.Position.TOP);
        navbar.setBrandName(Model.of("EEGbase"));
        
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, 
                new MyNavbarButton<ArticlesPage>(ArticlesPage.class, "menuItem.articles"),
                new MyNavbarButton<SearchPage>(SearchPage.class, "title.page.search.menu"),
                new MyNavbarButton<ListExperimentsByPackagePage>(ListExperimentsByPackagePage.class, "menuItem.experiments"),
                new MyNavbarButton<ListScenariosPage>(ListScenariosPage.class, "menuItem.scenarios"),
                new MyNavbarButton<ListResearchGroupsPage>(ListResearchGroupsPage.class, "menuItem.groups"),
                new MyNavbarButton<ListPersonPage>(ListPersonPage.class, "menuItem.people"),
                new MyNavbarButton<ListListsPage>(ListListsPage.class, "menuItem.lists"),
                new MyNavbarButton<HistoryPage>(HistoryPage.class, "menuItem.history"),
                new MyNavbarButton<AdminManageUserRolePage>(AdminManageUserRolePage.class, "menuItem.administration")  ));
        
        /*
        List<NavbarButton> buttons = new LinkedList<NavbarButton>();
        MenuItem menu = new MenuItem(EEGDatabaseMainMenu.Main);
        for (MenuItem item : menu.getSubmenu()) {
            String title = ResourceUtils.getString(item.getString());
            NavbarButton<?> button = new NavbarButton(item.getClassForUrl(), Model.of(title));
            buttons.add(button);
        }
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, buttons.toArray(new NavbarButton[0])));
        */
        
        if (EEGDataBaseSession.get().isSignedIn()) {
            navbar.addComponents(new ImmutableNavbarComponent(userDropdown(), Navbar.ComponentPosition.RIGHT));
        }
        
        return navbar;
    }
    
    
    private DropDownButton userDropdown() {
        String userName = EEGDataBaseSession.get().getLoggedUser().getUsername();
        
        // logout link
        @SuppressWarnings("serial")
        final LabeledLink logoutLink = new LabeledLink(ButtonList.getButtonMarkupId(), new ResourceModel("action.logout")) {
            
            @Override
            public void onClick() {
                EEGDataBaseSession.get().invalidate();
                setResponsePage(EEGDataBaseApplication.get().getHomePage());
            }
            
        };
        
        DropDownButton dropdown = new NavbarDropDownButton(Model.of(userName)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
                subMenu.add(new MenuBookmarkablePageLink(AccountOverViewPage.class, new ResourceModel("general.page.myaccount.link"))
                                .setIconType(FontAwesomeIconType.user));
                subMenu.add(new MenuBookmarkablePageLink(ShoppingCartPage.class,
                        new StringResourceModel("general.page.myCart.link", this, null, EEGDataBaseSession.get().getShoppingCart().size()))
                                .setIconType(FontAwesomeIconType.shopping_cart));
                subMenu.add(logoutLink);
                return subMenu;
            }

        };
        
        return dropdown;
    }
    
    
    
    private class MyNavbarButton<T extends Page> extends NavbarButton<T> {

        private static final long serialVersionUID = 2927676648479612362L;

        public MyNavbarButton(Class<T> pageClass, String resourceKey) {
            super(pageClass, new ResourceModel(resourceKey));
        }
        
        @Override
        public boolean isVisible() {
            AuthorizeInstantiation annotation = ((AuthorizeInstantiation) getPageClass().getAnnotation(AuthorizeInstantiation.class));
            if (annotation != null) {
                Roles roles = new Roles(annotation.value());
                return EEGDataBaseSession.get().hasAnyRole(roles);
            }
            return true;
        }
        
    }
    
    

}
