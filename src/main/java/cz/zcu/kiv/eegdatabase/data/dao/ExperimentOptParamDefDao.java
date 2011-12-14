package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface ExperimentOptParamDefDao extends GenericDao<ExperimentOptParamDef, Integer> {
    public List<ExperimentOptParamDef> getItemsForList();

    public boolean canDelete(int id);

     public List<ExperimentOptParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef);

    public List<ExperimentOptParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId);

    public void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup);

    public boolean isDefault(int id);
}
