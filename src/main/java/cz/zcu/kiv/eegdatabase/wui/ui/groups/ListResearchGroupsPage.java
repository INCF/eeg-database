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
 *   ListResearchGroupsPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.StyleClassPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;

/**
 * Page with list of research groups.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListResearchGroupsPage extends MenuPage {

    private static final long serialVersionUID = -4292371108898020659L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade securityFacade;

    public ListResearchGroupsPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfGroups"));
        setupComponents();
    }

    private void setupComponents() {

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        DefaultDataTable<ResearchGroup, String> list = new DefaultDataTable<ResearchGroup, String>("list", createListColumns(),
                new ListResearchGroupsDataProvider(researchGroupFacade), ITEMS_PER_PAGE);

        add(list);

    }

    private List<? extends IColumn<ResearchGroup, String>> createListColumns() {
        List<IColumn<ResearchGroup, String>> columns = new ArrayList<IColumn<ResearchGroup, String>>();

        columns.add(new StyleClassPropertyColumn<ResearchGroup, String>(ResourceUtils.getModel("dataTable.heading.groupTitle"), "title", "title", "columnGroupTitle"));
        columns.add(new StyleClassPropertyColumn<ResearchGroup, String>(ResourceUtils.getModel("dataTable.heading.description"), "description", "description", "columnGroupDescription"));
        columns.add(new PropertyColumn<ResearchGroup, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<ResearchGroup>> item, String componentId, IModel<ResearchGroup> rowModel) {
                item.add(new ViewLinkPanel(componentId, ResearchGroupsDetailPage.class, "researchGroupId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
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
