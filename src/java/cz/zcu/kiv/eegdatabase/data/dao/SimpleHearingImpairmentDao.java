package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;

import java.util.List;

public class SimpleHearingImpairmentDao extends SimpleGenericDao<HearingImpairment, Integer> implements HearingImpairmentDao {
    public SimpleHearingImpairmentDao() {
        super(HearingImpairment.class);
    }

    public List<HearingImpairment> getItemsForList() {
        String hqlQuery = "from HearingImpairment i order by i.description";
        List<HearingImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
