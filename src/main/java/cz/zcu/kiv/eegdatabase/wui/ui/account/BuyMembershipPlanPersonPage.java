package cz.zcu.kiv.eegdatabase.wui.ui.account;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.components.AddPersonalMembershipPlanToCartLink;
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
public class BuyMembershipPlanPersonPage extends MenuPage {


    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    PersonMembershipPlanFacade personMembershipPlanFacade;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    @SpringBean
    PromoCodeFacade promoCodeFacade;

    @SpringBean
    PersonFacade personFacade;

    public BuyMembershipPlanPersonPage(PageParameters parameters) {
        final Person person = personFacade.getLoggedPerson();

        setPageTitle(ResourceUtils.getModel("pageTitle.buyPlan"));

        add(new ButtonPageMenu("leftMenu", MyAccountPageLeftMenu.values()));

        Person user = EEGDataBaseSession.get().getLoggedUser();

        if (user == null)
            throw new RestartResponseAtInterceptPageException(HomePage.class);

        DefaultDataTable<MembershipPlan, String> personPlans = new DefaultDataTable<MembershipPlan, String>("personPlans", createMembershipListColumns(),
                new ListMembershipsDataProvider(membershipPlanFacade, MembershipPlanType.PERSON), ITEMS_PER_PAGE);

        add(personPlans);
    }

    private List<? extends IColumn<MembershipPlan, String>> createMembershipListColumns() {
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
                item.add(new AddPersonalMembershipPlanToCartLink(componentId, rowModel));
            }
        });
        return columns;
    }


}
