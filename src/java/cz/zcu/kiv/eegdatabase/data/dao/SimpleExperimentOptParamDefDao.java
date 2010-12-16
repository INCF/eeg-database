package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;

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
}
