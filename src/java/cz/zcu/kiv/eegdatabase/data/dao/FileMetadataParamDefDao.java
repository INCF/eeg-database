package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;

import java.util.List;

public interface FileMetadataParamDefDao extends GenericDao<FileMetadataParamDef, Integer> {
    public List<FileMetadataParamDef> getItemsForList();
}
