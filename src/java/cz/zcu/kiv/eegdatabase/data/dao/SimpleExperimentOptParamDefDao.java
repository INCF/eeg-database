package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;

import java.util.List;

public class SimpleExperimentOptParamDefDao extends SimpleGenericDao<ExperimentOptParamDef, Integer> implements ExperimentOptParamDefDao {
    public SimpleExperimentOptParamDefDao() {
        super(ExperimentOptParamDef.class);
    }

    public List<ExperimentOptParamDef> getItemsForList() {
        String hqlQuery = "from ExperimentOptParamDef i order by i.paramName";
        List<ExperimentOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select def.experimentOptParamVals from ExperimentOptParamDef def where def.experimentOptParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ExperimentOptParamDef> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
}
