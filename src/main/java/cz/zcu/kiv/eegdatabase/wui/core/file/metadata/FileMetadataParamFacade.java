package cz.zcu.kiv.eegdatabase.wui.core.file.metadata;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface FileMetadataParamFacade extends GenericFacade<FileMetadataParamDef, Integer> {

    List<FileMetadataParamDef> getItemsForList();

    boolean canDelete(int id);

    List<FileMetadataParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef);

    List<FileMetadataParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId);

    void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);
    
    FileMetadataParamValId create(FileMetadataParamVal newInstance);

    FileMetadataParamVal read(FileMetadataParamValId id);

    List<FileMetadataParamVal> readValueByParameter(String parameterName, int parameterValue);

    List<FileMetadataParamVal> readValueByParameter(String parameterName, String parameterValue);

    void update(FileMetadataParamVal transientObject);

    void delete(FileMetadataParamVal persistentObject);

    List<FileMetadataParamVal> getAllValueRecords();

    List<FileMetadataParamVal> getValueRecordsAtSides(int first, int max);

    int getValueCountRecords();
}
