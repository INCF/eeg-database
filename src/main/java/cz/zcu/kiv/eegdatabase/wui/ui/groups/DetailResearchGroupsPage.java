package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupDTO;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

@AuthorizeInstantiation("ROLE_USER")
public class DetailResearchGroupsPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    @SpringBean
    ResearchGroupFacade researchGroupFacade;

    public DetailResearchGroupsPage() {

        setPageTitle(ResourceUtils.getModel("title.page.groups"));
    }

    public DetailResearchGroupsPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("title.page.groups"));

        StringValue value = parseParameters(parameters);

        int groupId = value.toInt();
        ResearchGroupDTO groupDTO = researchGroupFacade.getResearchGroupById(groupId);

        add(new Label("title", groupDTO.getTitle()));
        add(new Label("description", groupDTO.getDescription()));

    }

    private StringValue parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value;
    }

}
