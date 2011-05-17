package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;

import java.util.List;

public interface ExperimentOptParamDefDao extends GenericDao<ExperimentOptParamDef, Integer> {
    public List<ExperimentOptParamDef> getItemsForList();

    public boolean canDelete(int id);
}
