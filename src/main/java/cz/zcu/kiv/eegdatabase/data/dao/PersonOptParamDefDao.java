package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface PersonOptParamDefDao extends GenericDao<PersonOptParamDef, Integer> {
    public List<PersonOptParamDef> getItemsForList();

    public boolean canDelete(int id);
    
     public List<PersonOptParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(PersonOptParamDef personOptParamDef);

    public List<PersonOptParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId);

    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup);

    public boolean isDefault(int id);
}
