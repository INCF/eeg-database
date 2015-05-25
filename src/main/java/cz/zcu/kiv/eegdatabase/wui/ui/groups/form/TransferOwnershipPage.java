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
 *   TransferOwnershipPage.java, 2015/05/14 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups.form;

import java.util.*;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GroupRole;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.GroupPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListOfMembersGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ResearchGroupsDetailPage;

/**
 * Page for transferOwnership action.
 *
 * @author stebjan
 */
@AuthorizeInstantiation(value = {"ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN"})
public class TransferOwnershipPage extends MenuPage {

    private static final long serialVersionUID = 7280002331574740721L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupFacade facade;

    @SpringBean
    PersonFacade personFacade;

    public TransferOwnershipPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.transferOwnership"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();

        if (!securityFacade.userIsAdminInGroup(groupId))
            throw new RestartResponseAtInterceptPageException(ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

        setupComponents(groupId);
    }

    private void setupComponents(final int groupId) {

        add(new Label("title", facade.getResearchGroupTitle(groupId)));

        add(new TransferOwnershipForm("form", facade, personFacade, getFeedback(), groupId));
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
            throw new RestartResponseAtInterceptPageException(ListResearchGroupsPage.class);

        return value;
    }

    // inner form for transfer ownership of group action.
    private class TransferOwnershipForm extends Form<Void> {

        private static final long serialVersionUID = 1L;

        public TransferOwnershipForm(String id, final ResearchGroupFacade facade, final PersonFacade personFacade, final FeedbackPanel feedback, final int groupId) {
            super(id);
            List<HashMap<String, Object>> choices = facade.getListOfGroupMembers(groupId);
            ChoiceRenderer renderer = new ChoiceRenderer("username", "personId");
            final DropDownChoice<HashMap<String, Object>> members = new DropDownChoice<HashMap<String, Object>>("members", new Model<HashMap<String, Object>>(), choices, renderer);
            members.setLabel(ResourceUtils.getModel("label.username"));
            members.setRequired(true);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.transferOwnership"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    HashMap<String, Object> personMap = members.getModelObject();


                    Person person = personFacade.getPerson((String) personMap.get("username"));
                    ResearchGroup group = facade.getResearchGroupById(groupId);
                    group.setPerson(person);
                    facade.update(group);
                    ResearchGroupMembership membership = facade.readMemberhip(new ResearchGroupMembershipId(person.getPersonId(), groupId));
                    membership.setAuthority(Util.GROUP_ADMIN);
                    facade.updateMemberhip(membership);

                    setResponsePage(ListOfMembersGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

                    target.add(feedback);
                }
            };
            add(submit, members);
        }
    }

}
