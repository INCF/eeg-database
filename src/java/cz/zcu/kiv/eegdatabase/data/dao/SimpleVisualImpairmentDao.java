package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;

import java.util.List;

public class SimpleVisualImpairmentDao extends SimpleGenericDao<VisualImpairment, Integer> implements VisualImpairmentDao {
    public SimpleVisualImpairmentDao() {
        super(VisualImpairment.class);
    }

    public List<VisualImpairment> getItemsForList() {
        String hqlQuery = "from VisualImpairment i order by i.description";
        List<VisualImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
