package cz.zcu.kiv.eegdatabase.wui.ui.groups.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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
 * Page for add member to group action.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class AddMemberToGroupPage extends MenuPage {

    private static final long serialVersionUID = 7280002331574740721L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupFacade facade;

    @SpringBean
    PersonFacade personFacade;

    public AddMemberToGroupPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.addMemberToGroup"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();

        if (!securityFacade.userIsAdminInGroup(groupId))
            throw new RestartResponseAtInterceptPageException(ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

        setupComponents(groupId);
    }

    private void setupComponents(final int groupId) {

        add(new Label("title", facade.getResearchGroupTitle(groupId)));

        add(new AddMemberForm("form", facade, personFacade, getFeedback(), groupId));
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
    
    // inner for for add member to group action.
    private class AddMemberForm extends Form<Void> {

        private static final long serialVersionUID = 1L;

        public AddMemberForm(String id, final ResearchGroupFacade facade, final PersonFacade personFacade, final FeedbackPanel feedback, final int groupId) {
            super(id);

            final TextField<String> usernameField = new TextField<String>("username", new Model<String>(""));
            usernameField.setRequired(true);
            usernameField.setLabel(ResourceUtils.getModel("label.email"));

            final DropDownChoice<GroupRole> roles = new DropDownChoice<GroupRole>("roles", new Model<GroupRole>(), Arrays.asList(GroupRole.values()),
                    new EnumChoiceRenderer<GroupRole>());
            roles.setLabel(ResourceUtils.getModel("label.userRole"));
            roles.setRequired(true);

            AjaxButton submit = new AjaxButton("submit" ,ResourceUtils.getModel("button.addMemberToGroup"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    String username = usernameField.getModelObject();
                    GroupRole role = roles.getModelObject();

                    if (validateForm(username, groupId)) {
                        Person person = personFacade.getPerson(username);
                        ResearchGroupMembershipId id = new ResearchGroupMembershipId(person.getPersonId(), groupId);
                        ResearchGroupMembership membership = new ResearchGroupMembership();
                        membership.setId(id);
                        membership.setAuthority(role.name());

                        facade.createMemberhip(membership);
                        setResponsePage(ListOfMembersGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
                    }

                    target.add(feedback);
                }
            };
            add(submit, usernameField, roles);
        }

        private boolean validateForm(String username, int groupId) {

            if (!personFacade.usernameExists(username)) {
                error(ResourceUtils.getString("invalid.userNameDoesNotExist"));
                return false;
            } else if (personFacade.userNameInGroup(username, groupId)) {
                error(ResourceUtils.getString("invalid.userNameAlreadyInGroup"));
                return false;
            }

            return true;
        }
    }

}
