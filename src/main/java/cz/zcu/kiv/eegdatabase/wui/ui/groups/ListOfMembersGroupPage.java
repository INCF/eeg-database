package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GroupRole;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.AddMemberToGroupPage;

/**
 * Page with list of members in research group.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListOfMembersGroupPage extends MenuPage {

    private static final long serialVersionUID = 7280002331574740721L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    SecurityFacade securityFacade;

    @SpringBean
    ResearchGroupFacade facade;

    public ListOfMembersGroupPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfGroupMembers"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();
        
        if (!securityFacade.userIsExperimenterInGroup(groupId))
            throw new RestartResponseAtInterceptPageException(ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

        setupComponents(groupId);
    }

    private void setupComponents(final int groupId) {

        add(new Label("title", facade.getResearchGroupTitle(groupId)));

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupPlaceholderTag(true);

        LoadableDetachableModel<List<Map<String, Object>>> listModel = new LoadableDetachableModel<List<Map<String, Object>>>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Map<String, Object>> load() {
                return facade.getListOfGroupMembers(groupId);
            }
        };

        final boolean isUserGroupAdmin = securityFacade.userIsAdminInGroup(groupId);

        PropertyListView<Map<String, Object>> members = new PropertyListView<Map<String, Object>>("members", listModel) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Map<String, Object>> item) {
                final Map<String, Object> person = item.getModelObject();

                final int personId = (Integer) person.get("personId");
                final ResearchGroupMembershipId id = new ResearchGroupMembershipId(personId, groupId);

                item.add(new Label("givenname", (String) person.get("givenname")));
                item.add(new Label("surname", (String) person.get("surname")));
                item.add(new Label("username", (String) (person.get("username") != null ? person.get("username") : "---")));
                GroupRole role = GroupRole.valueOf((String) person.get("authority"));
                item.add(new EnumLabel<GroupRole>("authority", role));

                final DropDownChoice<GroupRole> roles = new DropDownChoice<GroupRole>("roles", new Model<GroupRole>(role), Arrays.asList(GroupRole.values()),
                        new EnumChoiceRenderer<GroupRole>());
                roles.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {

                        GroupRole groupRole = roles.getModelObject();
                        ResearchGroupMembership membership = facade.readMemberhip(id);
                        membership.setAuthority(groupRole.name());
                        facade.updateMemberhip(membership);

                        setResponsePage(getPage());
                    }

                });

                roles.setVisibilityAllowed(isUserGroupAdmin);
                item.add(roles);

                item.add(new AjaxLink<Void>("removeFromGroupLink") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        ResearchGroupMembership membership = facade.readMemberhip(id);
                        facade.deleteMemberhip(membership);

                        setResponsePage(getPage());
                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
                    {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('" + ResourceUtils.getString("link.confirm.sureToRemoveUserFromGroup") + "');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }
                }.setVisibilityAllowed(isUserGroupAdmin));

            }
        };

        container.add(members);

        BookmarkablePageLink<Void> backToDetailLink = new BookmarkablePageLink<Void>("backLink", ResearchGroupsDetailPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        BookmarkablePageLink<Void> addMemberLink = new BookmarkablePageLink<Void>("addMemberLink", AddMemberToGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        addMemberLink.setVisibilityAllowed(isUserGroupAdmin);

        add(container, backToDetailLink, addMemberLink);

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
