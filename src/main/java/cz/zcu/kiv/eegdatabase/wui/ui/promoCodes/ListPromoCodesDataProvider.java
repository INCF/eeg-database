package cz.zcu.kiv.eegdatabase.wui.ui.promoCodes;

import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.promocode.PromoCodeFacade;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 21.4.15.
 */
public class ListPromoCodesDataProvider extends BasicDataProvider<PromoCode> {

    public ListPromoCodesDataProvider(PromoCodeFacade promoCodeFacade, MembershipPlanType type) {

        super("keyword", SortOrder.ASCENDING);
        List<PromoCode> codes;
        if(type == MembershipPlanType.GROUP) {
            codes = promoCodeFacade.getAvailableGroupPromoCodes();
        }
        else if (type == MembershipPlanType.PERSON){
            codes = promoCodeFacade.getAvailablePersonPromoCodes();
        }
        else {
           codes = new ArrayList<PromoCode>();
        }

        super.listModel.setObject(codes);
    }
}
