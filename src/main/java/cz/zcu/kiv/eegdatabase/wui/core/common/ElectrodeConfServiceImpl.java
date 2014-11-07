package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeConf;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;

public class ElectrodeConfServiceImpl extends GenericServiceImpl<ElectrodeConf, Integer> implements ElectrodeConfService {

    public ElectrodeConfServiceImpl() {
        
    }

    public ElectrodeConfServiceImpl(GenericDao<ElectrodeConf, Integer> dao) {
        super(dao);
    }
}
