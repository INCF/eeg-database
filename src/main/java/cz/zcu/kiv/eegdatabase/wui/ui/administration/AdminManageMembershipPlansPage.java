package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.MembershipPlan;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxConfirmLink;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.DeleteLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimeStampPanel;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.MembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.ResearchGroupMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.MyAccountPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.forms.MembershipPlanManageFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.forms.PromoCodeManageFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;
import cz.zcu.kiv.eegdatabase.wui.ui.memberships.ListMembershipsDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.memberships.MembershipPlansDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.form.PersonFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.promoCodes.ListPromoCodesDataProvider;
import cz.zcu.kiv.eegdatabase.wui.ui.promoCodes.PromoCodeDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.signalProcessing.MethodListPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 6.4.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN"})
public class AdminManageMembershipPlansPage extends MenuPage {

    private static final long serialVersionUID = -5514198024012232250L;

    private String timestampFormat ="dd.MM.yyyy";

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    MembershipPlanFacade membershipPlanFacade;

    @SpringBean
    PromoCodeFacade promoCodeFacade;


    public AdminManageMembershipPlansPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.membershipPlans"));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        Person user = EEGDataBaseSession.get().getLoggedUser();

        if (user == null)
            throw new RestartResponseAtInterceptPageException(HomePage.class);

        DefaultDataTable<MembershipPlan, String> personPlans = new DefaultDataTable<MembershipPlan, String>("personPlans", createMembershipListColumns(),
                new ListMembershipsDataProvider(membershipPlanFacade, MembershipPlanType.PERSON), ITEMS_PER_PAGE);

        DefaultDataTable<MembershipPlan, String> groupPlans = new DefaultDataTable<MembershipPlan, String>("groupPlans", createMembershipListColumns(),
                new ListMembershipsDataProvider(membershipPlanFacade, MembershipPlanType.GROUP), ITEMS_PER_PAGE);

        BookmarkablePageLink<Void> addPlan = new BookmarkablePageLink<Void>("addPlan", MembershipPlanManageFormPage.class);

        add(personPlans,groupPlans,addPlan);


        DefaultDataTable<PromoCode, String> personPromoCodes = new DefaultDataTable<PromoCode, String>("personPromoCodes", createPromoCodeListColumns(),
                new ListPromoCodesDataProvider(promoCodeFacade, MembershipPlanType.PERSON), ITEMS_PER_PAGE);

        DefaultDataTable<PromoCode, String> groupPromoCodes = new DefaultDataTable<PromoCode, String>("groupPromoCodes", createPromoCodeListColumns(),
                new ListPromoCodesDataProvider(promoCodeFacade, MembershipPlanType.GROUP), ITEMS_PER_PAGE);


        BookmarkablePageLink<Void> addPromoCode = new BookmarkablePageLink<Void>("addPromoCode", PromoCodeManageFormPage.class);

        add(personPromoCodes,groupPromoCodes,addPromoCode);

    }

    private List<? extends IColumn<MembershipPlan, String>> createMembershipListColumns() {
        List<IColumn<MembershipPlan, String>> columns = new ArrayList<IColumn<MembershipPlan, String>>();

        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.membershipName"), "name", "name"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.dayslength"), "length", "length"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.price"), "price", "price"));
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<MembershipPlan>> item, String componentId, IModel<MembershipPlan> rowModel) {
                item.add(new ViewLinkPanel(componentId, MembershipPlansDetailPage.class, "membershipId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        columns.add(new PropertyColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.edit"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<MembershipPlan>> item, String componentId, IModel<MembershipPlan> rowModel) {
                item.add(new ViewLinkPanel(componentId, MembershipPlanManageFormPage.class, "membershipId", rowModel, ResourceUtils.getModel("link.edit")));
            }
        });
        columns.add(new AbstractColumn<MembershipPlan, String>(ResourceUtils.getModel("dataTable.heading.delete")) {
            @Override
            public void populateItem(Item<ICellPopulator<MembershipPlan>> item, String componentId,final IModel<MembershipPlan> rowModel) {

                item.add(new DeleteLinkPanel(componentId,AdminManageMembershipPlansPage.class,"membershipId", rowModel,ResourceUtils.getModel("link.delete"),ResourceUtils.getString("text.delete.membershipplan")));
            }
        });
        return columns;
    }

    private List<? extends IColumn<PromoCode, String>> createPromoCodeListColumns() {
        List<IColumn<PromoCode, String>> columns = new ArrayList<IColumn<PromoCode, String>>();

        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.keyword"), "keyword", "keyword"));
        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.discount"), "discount", "discount"));
        
        // XXX why create TimeStampPanel if there is TimestampPropertyColumn for wicket tables already implemented ???
        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.from"), "from", "from") {
            @Override
            public void populateItem(Item<ICellPopulator<PromoCode>> item, String componentId, IModel<PromoCode> rowModel) {
                item.add(new TimeStampPanel(componentId, "from", rowModel, timestampFormat));
            }
        });
        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.to"), "to", "to"){
            @Override
            public void populateItem(Item<ICellPopulator<PromoCode>> item, String componentId, IModel<PromoCode> rowModel) {
                item.add(new TimeStampPanel(componentId, "to", rowModel, timestampFormat));
            }
        });
        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PromoCode>> item, String componentId, IModel<PromoCode> rowModel) {
                item.add(new ViewLinkPanel(componentId, PromoCodeDetailPage.class, "promoCodeId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });
        columns.add(new PropertyColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.edit"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PromoCode>> item, String componentId, IModel<PromoCode> rowModel) {
                item.add(new ViewLinkPanel(componentId, PromoCodeManageFormPage.class, "promoCodeId", rowModel, ResourceUtils.getModel("link.edit")));
            }
        });
        columns.add(new AbstractColumn<PromoCode, String>(ResourceUtils.getModel("dataTable.heading.delete")) {
            @Override
            public void populateItem(Item<ICellPopulator<PromoCode>> item, String componentId,final IModel<PromoCode> rowModel) {

                item.add(new DeleteLinkPanel(componentId,AdminManageMembershipPlansPage.class,"promoCodeId", rowModel,ResourceUtils.getModel("link.delete"),ResourceUtils.getString("text.delete.promocode")));
            }
        });
        return columns;
    }
}
