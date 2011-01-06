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

    public boolean canSaveDescription(String description, int id) {
        String hqlQuery = "from HearingImpairment i where i.description = :description and i.hearingImpairmentId != :id";
        String[] names = {"description", "id"};
        Object[] values = {description, id};
        List<HearingImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
}
