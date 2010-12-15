package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;

import java.util.List;

public class SimplePersonOptParamDefDao extends SimpleGenericDao<PersonOptParamDef, Integer> implements PersonOptParamDefDao {
    public SimplePersonOptParamDefDao() {
        super(PersonOptParamDef.class);
    }

    public List<PersonOptParamDef> getItemsForList() {
        String hqlQuery = "from PersonOptParamDef i order by i.paramName";
        List<PersonOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
