package cz.zcu.kiv.eegdatabase.wui.core.promocode;

import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by Lichous on 20.4.15.
 */
public class PromoCodeFacadeImpl extends GenericFacadeImpl<PromoCode, Integer> implements PromoCodeFacade {

    protected Log log = LogFactory.getLog(getClass());
    private PromoCodeService service;

    public PromoCodeFacadeImpl(PromoCodeService promoCodeService) {
        super(promoCodeService);
        setService(promoCodeService);
    }

    @Required
    public void setService(PromoCodeService service) {
        this.service = service;
    }



    @Override
    public List<PromoCode> getAvailableGroupPromoCodes() {
        return service.getAvailableGroupPromoCodes();
    }

    @Override
    public List<PromoCode> getAvailablePersonPromoCodes() {
        return service.getAvailablePersonPromoCodes();
    }

    @Override
    public PromoCode getPromoCodeById(Integer id) {
        return service.getPromoCodeById(id);
    }

    @Override
    public PromoCode getPromoCodeByKeyword(String keyWord)  {
        return service.getPromoCodeByKeyword(keyWord);
    }
}
