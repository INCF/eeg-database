package cz.zcu.kiv.eegdatabase.wui.ui.memberships;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageMembershipPlansPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;


/**
 * Created by Lichous on 15.4.15.
 */

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class MembershipPlansDetailPage extends MenuPage {

    private static final long serialVersionUID = 2054442473728190019L;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;


    public MembershipPlansDetailPage(PageParameters parameters) {
        StringValue membershipPlanId = parameters.get(DEFAULT_PARAM_ID);
        if (membershipPlanId.isNull() || membershipPlanId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageMembershipPlansPage.class);

        setupPageComponents(membershipPlanId.toInteger());

    }

    private void setupPageComponents(final Integer membershipPlanId) {

        Person user = EEGDataBaseSession.get().getLoggedUser();

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()).setVisibilityAllowed(user.getAuthority().equals(UserRole.ROLE_ADMIN.toString())));

        final MembershipPlan membershipPlan = membershipPlanFacade.getMembershipPlanById(membershipPlanId);

        add(new Label("name", membershipPlan.getName()));
        add(new Label("description", membershipPlan.getDescription()));
        add(new Label("price", membershipPlan.getPrice()));
        add(new Label("length", membershipPlan.getLength()));
        add(new Label("type", MembershipPlanType.getMembershipPlanByType(membershipPlan.getType())));


    }

}
