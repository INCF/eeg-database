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
 *   MyGroupsPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;

/**
 * Page with list of my research group page.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class MyGroupsPage extends MenuPage {

    private static final long serialVersionUID = 7642482805572959893L;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade securityFacade;

    public MyGroupsPage() {
        setPageTitle(ResourceUtils.getModel("pageTitle.myGroups"));
        setupComponents();
    }

    private void setupComponents() {
        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        List<ResearchGroup> ownedGroups = researchGroupFacade.getResearchGroupsWhereOwner(EEGDataBaseSession.get().getLoggedUser());
        List<ResearchGroup> memberGroups = researchGroupFacade.getResearchGroupsWhereMember(EEGDataBaseSession.get().getLoggedUser());

        PropertyListView<ResearchGroup> ownedGroupsList = new PropertyListView<ResearchGroup>("ownedGroups", new ListModel<ResearchGroup>(ownedGroups)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroup> item) {
                item.setModel(new CompoundPropertyModel<ResearchGroup>(item.getModel()));
                item.add(new Label("title"));
                item.add(new Label("description"));
                item.add(new ViewLinkPanel("detail", ResearchGroupsDetailPage.class, "researchGroupId", item.getModel(), ResourceUtils.getModel("link.detail")));
            }
        };

        PropertyListView<ResearchGroup> memberGroupsList = new PropertyListView<ResearchGroup>("memberGroups", new ListModel<ResearchGroup>(memberGroups)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroup> item) {
                item.setModel(new CompoundPropertyModel<ResearchGroup>(item.getModel()));
                item.add(new Label("title"));
                item.add(new Label("description"));
                item.add(new ViewLinkPanel("detail", ResearchGroupsDetailPage.class, "researchGroupId", item.getModel(), ResourceUtils.getModel("link.detail")));
            }
        };

        add(ownedGroupsList, memberGroupsList);
    }

    private GroupPageLeftMenu[] prepareLeftMenu() {

        List<GroupPageLeftMenu> list = new ArrayList<GroupPageLeftMenu>();
        boolean authorizedToRequestGroupRole = securityFacade.isAuthorizedToRequestGroupRole();

        for (GroupPageLeftMenu tmp : GroupPageLeftMenu.values())
            list.add(tmp);

        if (!authorizedToRequestGroupRole)
            list.remove(GroupPageLeftMenu.REQUEST_FOR_GROUP_ROLE);

        GroupPageLeftMenu[] array = new GroupPageLeftMenu[list.size()];
        return list.toArray(array);
    }

}
