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
 *   ListOfMembersGroupPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipPlan;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.ResearchGroupMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Page with list of members in research group.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListOfMembershipPlansGroupPage extends MenuPage {

    private static final long serialVersionUID = 7280002331574740721L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    @SpringBean
    ResearchGroupMembershipPlanFacade researchGroupMembershipPlanFacade;

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;



    public ListOfMembershipPlansGroupPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.listGroupMembershipPlans"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();
        
        if (!securityFacade.userIsExperimenterInGroup(groupId))
            throw new RestartResponseAtInterceptPageException(ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

        setupComponents(groupId);
    }

    private void setupComponents(final int groupId) {
        ResearchGroup group = groupFacade.getResearchGroupById(groupId);
        final boolean isUserGroupAdmin = securityFacade.userIsAdminInGroup(groupId);

        //add(new Label("title", groupFacade.getResearchGroupTitle(groupId)));

        //final WebMarkupContainer container = new WebMarkupContainer("container");
        //container.setOutputMarkupPlaceholderTag(true);

        List<ResearchGroupMembershipPlan> plansList = researchGroupMembershipPlanFacade.getGroupMembershipPlans(group);
        List<ResearchGroupMembershipPlan> activePlansList = new ArrayList<ResearchGroupMembershipPlan>();
        List<ResearchGroupMembershipPlan> expiredPlansList = new ArrayList<ResearchGroupMembershipPlan>();

        for(ResearchGroupMembershipPlan plan : plansList)
        {
            if(plan.getTo().after(new Date(System.currentTimeMillis())))
                  activePlansList.add(plan);
            else expiredPlansList.add(plan);

        }

        ListView<ResearchGroupMembershipPlan> groupPlansActive = new ListView<ResearchGroupMembershipPlan>("groupPlansActive", activePlansList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroupMembershipPlan> item) {
                ResearchGroupMembershipPlan modelObject = item.getModelObject();
                item.add(new Label("name", modelObject.getMembershipPlan().getName()));
                item.add(new Label("price", modelObject.getMembershipPlan().getPrice()));
                item.add(new Label("from", modelObject.getFrom()));
                item.add(new Label("to", modelObject.getTo()));
            }
        };

        ListView<ResearchGroupMembershipPlan> groupPlansExpired = new ListView<ResearchGroupMembershipPlan>("groupPlansExpired", expiredPlansList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ResearchGroupMembershipPlan> item) {
                ResearchGroupMembershipPlan modelObject = item.getModelObject();
                item.add(new Label("name", modelObject.getMembershipPlan().getName()));
                item.add(new Label("price", modelObject.getMembershipPlan().getPrice()));
                item.add(new Label("from", modelObject.getFrom()));
                item.add(new Label("to", modelObject.getTo()));
            }
        };

        //container.add(groupPlansActive);
        //container.add(groupPlansExpired);

        BookmarkablePageLink<Void> backToDetailLink = new BookmarkablePageLink<Void>("backLink", ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        BookmarkablePageLink<Void> buyPlanLink = new BookmarkablePageLink<Void>("buyMembershipPlan", BuyMembershipPlanGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        buyPlanLink.setVisibilityAllowed(isUserGroupAdmin);

        add(groupPlansActive, groupPlansExpired, backToDetailLink, buyPlanLink);

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
