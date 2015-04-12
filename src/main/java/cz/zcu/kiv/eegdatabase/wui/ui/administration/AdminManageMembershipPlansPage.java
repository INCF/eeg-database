package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.MyAccountPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.forms.MembershipPlanManageFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN"})
public class AdminManageMembershipPlansPage extends MenuPage {

    private static final long serialVersionUID = -5514198024012232250L;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    public AdminManageMembershipPlansPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.membershipPlans"));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        Person user = EEGDataBaseSession.get().getLoggedUser();

        if (user == null)
            throw new RestartResponseAtInterceptPageException(HomePage.class);


        List<MembershipPlan> personMembershipPlanList = membershipPlanFacade.getAvailablePersonMembershipPlans();
        List<MembershipPlan> groupMembershipPlanList = membershipPlanFacade.getAvailableGroupMembershipPlans();



        ListView<MembershipPlan> personPlans = new ListView<MembershipPlan>("personPlans", personMembershipPlanList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<MembershipPlan> item) {
                MembershipPlan modelObject = item.getModelObject();
                item.add(new Label("name", modelObject.getName()));
                item.add(new Label("price", modelObject.getPrice()));
                item.add(new Label("length",modelObject.getLength()));
            }
        };

        ListView<MembershipPlan> groupPlans = new ListView<MembershipPlan>("groupPlans", groupMembershipPlanList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<MembershipPlan> item) {
                MembershipPlan modelObject = item.getModelObject();
                item.add(new Label("name", modelObject.getName()));
                item.add(new Label("price", modelObject.getPrice()));
                item.add(new Label("length",modelObject.getLength()));
            }
        };

        BookmarkablePageLink<Void> addPlan = new BookmarkablePageLink<Void>("addPlan", MembershipPlanManageFormPage.class, PageParametersUtils.getDefaultPageParameters(user.getPersonId()));

        add(personPlans, groupPlans,addPlan);
        //throw new RestartResponseAtInterceptPageException(UnderConstructPage.class);
    }
}
