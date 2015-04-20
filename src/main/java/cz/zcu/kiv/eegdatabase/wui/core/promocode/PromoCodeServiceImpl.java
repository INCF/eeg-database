package cz.zcu.kiv.eegdatabase.wui.core.promocode;

import cz.zcu.kiv.eegdatabase.data.dao.PromoCodeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PromoCode;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Lichous on 20.4.15.
 */
public class PromoCodeServiceImpl extends GenericServiceImpl<PromoCode, Integer> implements PromoCodeService{

    protected Log log = LogFactory.getLog(getClass());
    private PromoCodeDao dao;

    public PromoCodeServiceImpl(PromoCodeDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(PromoCodeDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoCode> getAvailableGroupPromoCodes() {
        return dao.getAvailableGroupPromoCodes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoCode> getAvailablePersonPromoCodes() {
        return dao.getAvailablePersonPromoCodes();
    }

    @Override
    @Transactional(readOnly = true)
    public PromoCode getPromoCodeById(Integer id) {
        return dao.getPromoCodeById(id);
    }
}
