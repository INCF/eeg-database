package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.AddMemberToGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.ResearchGroupFormPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ResearchGroupsDetailPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade securityFacade;

    public ResearchGroupsDetailPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.researchGroupDetail"));
    }

    public ResearchGroupsDetailPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.researchGroupDetail"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();
        ResearchGroup group = researchGroupFacade.getResearchGroupById(groupId);

        add(new Label("title", group.getTitle()));
        add(new Label("description", group.getDescription()));

        Link<Void> requestMembership = new Link<Void>("request") {

            @Override
            public void onClick() {
                // TODO missing action

            }
        };
        requestMembership.setVisibilityAllowed(!securityFacade.userIsMemberInGroup(groupId));

        BookmarkablePageLink<Void> listOfMembers = new BookmarkablePageLink<Void>("listOfMembers", ListOfMembersGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        BookmarkablePageLink<Void> addMember = new BookmarkablePageLink<Void>("addMember", AddMemberToGroupPage.class, PageParametersUtils.getDefaultPageParameters(groupId));
        BookmarkablePageLink<Void> editGroup = new BookmarkablePageLink<Void>("editGroup", ResearchGroupFormPage.class, PageParametersUtils.getDefaultPageParameters(groupId));

        boolean userIsAdminInGroup = securityFacade.userIsAdminInGroup(groupId);
        listOfMembers.setVisibilityAllowed(securityFacade.userIsExperimenterInGroup(groupId));
        addMember.setVisibilityAllowed(userIsAdminInGroup);

        editGroup.setVisibilityAllowed(userIsAdminInGroup);

        add(requestMembership, listOfMembers, addMember, editGroup);
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
