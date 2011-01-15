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

    public boolean canSaveDescription(String description, int id) {
        String hqlQuery = "from VisualImpairment i where i.description = :description and i.visualImpairmentId != :id";
        String[] names = {"description", "id"};
        Object[] values = {description, id};
        List<VisualImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select i.persons from VisualImpairment i where i.visualImpairmentId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<VisualImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
}
