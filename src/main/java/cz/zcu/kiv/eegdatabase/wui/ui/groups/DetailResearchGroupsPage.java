package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupDTO;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;

@AuthorizeInstantiation("ROLE_USER")
public class DetailResearchGroupsPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    @SpringBean
    SecurityFacade securityFacade;

    public DetailResearchGroupsPage() {

        setPageTitle(ResourceUtils.getModel("title.page.groups"));
    }

    public DetailResearchGroupsPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("title.page.groups"));

        add(new ButtonPageMenu("leftMenu", prepareLeftMenu()));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();
        ResearchGroupDTO groupDTO = researchGroupFacade.getResearchGroupById(groupId);

        add(new Label("title", groupDTO.getTitle()));
        add(new Label("description", groupDTO.getDescription()));

        Link<Void> requestMembership = new Link<Void>("request") {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        };
        requestMembership.setVisibilityAllowed(!securityFacade.userIsMemberInGroup(groupId));
        Link<Void> listOfMembers = new Link<Void>("listOfMembers") {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        };
        listOfMembers.setVisibilityAllowed(securityFacade.userIsExperimenterInGroup(groupId));
        Link<Void> addMember = new Link<Void>("addMember") {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        };
        boolean userIsAdminInGroup = securityFacade.userIsAdminInGroup(groupId);
        addMember.setVisibilityAllowed(userIsAdminInGroup);
        Link<Void> editGroup = new Link<Void>("editGroup") {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        };
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
