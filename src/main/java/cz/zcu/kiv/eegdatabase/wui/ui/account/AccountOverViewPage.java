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
 *   AccountOverViewPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.account;

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupAccountInfo;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;

/**
 * Account overview page with logged user information
 * 
 * @author Jakub Rinkes
 * 
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class AccountOverViewPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    PersonFacade personFacade;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    public AccountOverViewPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.accountOverview"));

        add(new ButtonPageMenu("leftMenu", MyAccountPageLeftMenu.values()));

        Person user = EEGDataBaseSession.get().getLoggedUser();

        if (user == null)
            throw new RestartResponseAtInterceptPageException(HomePage.class);

        // user information
        add(new Label("userName", new PropertyModel<String>(user, "email")));
        add(new Label("fullName", user.getGivenname() + " " + user.getSurname()));
        add(new Label("authority", new PropertyModel<String>(user, "authority")));

        List<ResearchGroupAccountInfo> groupDataForAccountOverview = researchGroupFacade.getGroupDataForAccountOverview(user);
        boolean emptyGroups = groupDataForAccountOverview.isEmpty();

        WebMarkupContainer noGroups = new WebMarkupContainer("noGroups");
        noGroups.setVisibilityAllowed(emptyGroups);

        // list of user groups
        ListView<ResearchGroupAccountInfo> groups = new ListView<ResearchGroupAccountInfo>("groups", groupDataForAccountOverview) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroupAccountInfo> item) {
                ResearchGroupAccountInfo modelObject = item.getModelObject();
                item.add(new Label("title", modelObject.getTitle()));
                item.add(new Label("authority", modelObject.getAuthority()));
                item.add(new BookmarkablePageLink<ResearchGroupsDetailPage>("link", ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(modelObject.getGroupId())));
            }
        };
        groups.setVisibilityAllowed(!emptyGroups);
        
        BookmarkablePageLink<Void> editAccount = new BookmarkablePageLink<Void>("editLink", PersonFormPage.class, PageParametersUtils.getDefaultPageParameters(user.getPersonId()));

        add(groups, noGroups, editAccount);
    }
}
