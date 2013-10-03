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
 *   GroupRoleRequestPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.springframework.mail.MailException;

import cz.zcu.kiv.eegdatabase.data.pojo.GroupPermissionRequest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GroupRole;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.GroupPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.ListResearchGroupsPage;

/**
 * Page for creating request for group roles.
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class GroupRoleRequestPage extends MenuPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupFacade facade;

    public GroupRoleRequestPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.requestForGroupRole"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        add(new GroupRoleRequestForm("form", new Model<GroupPermissionRequest>(new GroupPermissionRequest()), facade, getFeedback()));
    }

    public GroupRoleRequestPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.requestForGroupRole"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();

        add(new GroupRoleRequestForm("form", new Model<GroupPermissionRequest>(new GroupPermissionRequest()), facade, getFeedback()));
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

    private class GroupRoleRequestForm extends Form<GroupPermissionRequest> {

        private static final long serialVersionUID = 1L;

        public GroupRoleRequestForm(String id, IModel<GroupPermissionRequest> model, final ResearchGroupFacade facade, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<GroupPermissionRequest>(model));
            setOutputMarkupPlaceholderTag(true);
            final Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
            List<ResearchGroup> groupsWhereMember = facade.getResearchGroupsWhereMember(loggedUser);

            if (loggedUser.getDefaultGroup() != null)
                getModelObject().setResearchGroup(facade.getResearchGroupById(loggedUser.getDefaultGroup().getResearchGroupId()));

            DropDownChoice<ResearchGroup> groups = new DropDownChoice<ResearchGroup>("researchGroup", groupsWhereMember, new ChoiceRenderer<ResearchGroup>("title"));
            groups.setRequired(true);

            RadioChoice<String> roles = new RadioChoice<String>("requestedPermission", GroupRole.getListOfNames(), new ChoiceRenderer<String>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Object getDisplayValue(String object) {
                    GroupRole enumValue = GroupRole.valueOf(object);
                    return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
                }
            });

            final AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.sendRequest"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    GroupPermissionRequest object = GroupRoleRequestForm.this.getModelObject();
                    object.setPerson(loggedUser);

                    try {
                        facade.createPermissionRequest(object);
                        info("Request was send.");
                        setVisibilityAllowed(false);
                        target.add(this);
                    } catch (MailException e) {
                        error("Request was not send.");
                    }
                    target.add(feedback);
                }
            };

            add(submit, groups, roles);
        }
    }
}
