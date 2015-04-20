package cz.zcu.kiv.eegdatabase.wui.core.promocode;

import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

import java.util.List;

/**
 * Created by Lichous on 20.4.15.
 */
public interface PromoCodeFacade extends GenericFacade<PromoCode, Integer> {

    public List<PromoCode> getAvailableGroupPromoCodes();

    public List<PromoCode> getAvailablePersonPromoCodes();

    public PromoCode getPromoCodeById(Integer id);
}
