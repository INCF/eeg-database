package cz.zcu.kiv.eegdatabase.wui.core.person.param;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface PersonOptParamService extends GenericService<PersonOptParamDef, Integer>{
    
    List<PersonOptParamDef> getItemsForList();

    boolean canDelete(int id);

    List<PersonOptParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(PersonOptParamDef personOptParamDef);

    List<PersonOptParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId);

    void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);

    PersonOptParamValId create(PersonOptParamVal newInstance);

    PersonOptParamVal read(PersonOptParamValId id);

    List<PersonOptParamVal> readValueByParameter(String parameterName, int parameterValue);

    List<PersonOptParamVal> readValueByParameter(String parameterName, String parameterValue);

    void update(PersonOptParamVal transientObject);

    void delete(PersonOptParamVal persistentObject);

    List<PersonOptParamVal> getAllValueRecords();

    List<PersonOptParamVal> getValueRecordsAtSides(int first, int max);

    int getValueCountRecords();
}
