package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;

import java.util.List;

public class SimpleFileMetadataParamDefDao extends SimpleGenericDao<FileMetadataParamDef, Integer> implements FileMetadataParamDefDao {
    public SimpleFileMetadataParamDefDao() {
        super(FileMetadataParamDef.class);
    }

    public List<FileMetadataParamDef> getItemsForList() {
        String hqlQuery = "from FileMetadataParamDef d order by d.paramName";
        List<FileMetadataParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
