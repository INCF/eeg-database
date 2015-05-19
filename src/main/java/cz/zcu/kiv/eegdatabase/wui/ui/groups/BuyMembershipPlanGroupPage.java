package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.components.AddGroupMembershipPlanToCartLink;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.memberships.ListMembershipsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.memberships.MembershipPlansDetailPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * BuyMembershipPlanPage, 2015/04/07 05:54 administrator
 * <p/>
 * ********************************************************************************************************************
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class BuyMembershipPlanGroupPage extends MenuPage {


    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    MembershipPlanFacade planFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public BuyMembershipPlanGroupPage(PageParameters parameters) {
        StringValue value = parseParameters(parameters);
        final int groupId = value.toInt();
        final ResearchGroup group = groupFacade.getResearchGroupById(groupId);

        setPageTitle(ResourceUtils.getModel("pageTitle.buyPlan"));
        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        Person user = EEGDataBaseSession.get().getLoggedUser();

        if (user == null)
            throw new RestartResponseAtInterceptPageException(HomePage.class);

        DefaultDataTable<MembershipPlan, String> personPlans = new DefaultDataTable<MembershipPlan, String>("groupPlans", createMembershipListColumns(groupId),
                new ListMembershipsDataProvider(planFacade, MembershipPlanType.GROUP), ITEMS_PER_PAGE);

        add(personPlans);
    }

    private List<? extends IColumn<MembershipPlan, String>> createMembershipListColumns(final int groupID) {
        List<IColumn<MembershipPlan, String>> columns = new ArrayList<IColumn<MembershipPlan, String>>();

        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.membershipName"), "name", "name"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.dayslength"), "length", "length"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.price"), "price", "price"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<MembershipPlan>> item, String componentId, IModel<MembershipPlan> rowModel) {
                item.add(new ViewLinkPanel(componentId, MembershipPlansDetailPage.class, "membershipId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.buy"), null, null) {

            @Override
            public void populateItem(Item<ICellPopulator<MembershipPlan>> item, String componentId, IModel<MembershipPlan> rowModel) {
                item.add(new AddGroupMembershipPlanToCartLink(componentId, rowModel, groupFacade.getResearchGroupById(groupID)));
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

    private StringValue parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value;
    }
}
