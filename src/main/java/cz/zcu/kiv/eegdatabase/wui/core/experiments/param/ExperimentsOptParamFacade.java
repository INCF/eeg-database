package cz.zcu.kiv.eegdatabase.wui.core.experiments.param;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface ExperimentsOptParamFacade extends GenericFacade<ExperimentOptParamDef, Integer> {

    List<ExperimentOptParamDef> getItemsForList();

    boolean canDelete(int id);

    List<ExperimentOptParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef);

    List<ExperimentOptParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId);

    void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);
}
