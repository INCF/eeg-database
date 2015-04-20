package cz.zcu.kiv.eegdatabase.wui.ui.promoCodes;

import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageMembershipPlansPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

/**
 * Created by Lichous on 20.4.15.
 */
public class PromoCodeDetailPage extends MenuPage {

    private static final long serialVersionUID = 6910619546343835996L;

    @SpringBean
    PromoCodeFacade promoCodeFacade;

    public PromoCodeDetailPage(PageParameters parameters) {

        StringValue promoCodeId = parameters.get(DEFAULT_PARAM_ID);
        if (promoCodeId.isNull() || promoCodeId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageMembershipPlansPage.class);

        setupPageComponents(promoCodeId.toInteger());

    }

    private void setupPageComponents(final Integer id) {

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));

        final PromoCode promoCode = promoCodeFacade.getPromoCodeById(id);

        add(new Label("keyword",promoCode.getKeyword()));
        add(new Label("description", promoCode.getDescription()));
        add(new Label("discount",promoCode.getDiscount()));
        add(new Label("dateFrom", promoCode.getFrom()));
        add(new Label("dateTo", promoCode.getTo()));
        add(new Label("type", MembershipPlanType.getMembershipPlanByType(promoCode.getType())));

    }

}
