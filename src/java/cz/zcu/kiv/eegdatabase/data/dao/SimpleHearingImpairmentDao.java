package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;

public class SimpleHearingImpairmentDao extends SimpleGenericDao<HearingImpairment, Integer> implements HearingImpairmentDao {
    public SimpleHearingImpairmentDao() {
        super(HearingImpairment.class);
    }
}
