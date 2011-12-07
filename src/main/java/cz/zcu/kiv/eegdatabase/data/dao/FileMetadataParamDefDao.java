package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;

import java.util.List;

public interface FileMetadataParamDefDao extends GenericDao<FileMetadataParamDef, Integer> {
    public List<FileMetadataParamDef> getItemsForList();

    public boolean canDelete(int id);

    public List<FileMetadataParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef);

    public List<FileMetadataParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId);

    public void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel);

    public boolean isDefault(int id);
}
