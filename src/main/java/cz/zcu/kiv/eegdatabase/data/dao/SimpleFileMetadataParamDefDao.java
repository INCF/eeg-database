package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
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

    public boolean canDelete(int id) {
        String hqlQuery = "select def.fileMetadataParamVals from FileMetadataParamDef def where def.fileMetadataParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<FileMetadataParamDef> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
    
    public List<FileMetadataParamDef> getRecordsByGroup(int groupId){
        String hqlQuery = "from FileMetadataParamDef h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        List<FileMetadataParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef){
        fileMetadataParamDef.setDefaultNumber(1);
        create(fileMetadataParamDef);
    }

    public List<FileMetadataParamDef> getDefaultRecords(){
        String hqlQuery = "from FileMetadataParamDef h where h.defaultNumber=1";
        List<FileMetadataParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean hasGroupRel(int id){
        String hqlQuery = "from FileMetadataParamDefGroupRel r where r.id.fileMetadataParamDefId =" +id+ " ";
        List<FileMetadataParamDefGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() > 0);
    }

    public void deleteGroupRel(FileMetadataParamDefGroupRel
        fileMetadataParamDefGroupRel){
        getHibernateTemplate().delete(fileMetadataParamDefGroupRel);
    }

    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId){
        String hqlQuery = "from FileMetadataParamDefGroupRel r where r.id.fileMetadataParamDefId="+fileMetadataParamDefId+" and r.id.researchGroupId="+researchGroupId+" ";
        List<FileMetadataParamDefGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return list.get(0);
    }

    public void createGroupRel(FileMetadataParamDefGroupRel
        fileMetadataParamDefGroupRel){
        fileMetadataParamDefGroupRel.getFileMetadataParamDef().setDefaultNumber(0);
        getHibernateTemplate().save(fileMetadataParamDefGroupRel);
    }

    public void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup){
        fileMetadataParamDef.getResearchGroups().add(researchGroup);
        researchGroup.getFileMetadataParamDefs().add(fileMetadataParamDef);
    }
    
    public boolean isDefault(int id){
        String hqlQuery = "select h.defaultNumber from FileMetadataParamDef h where h.fileMetadataParamDefId="+id+" ";
        List<Integer> list = getHibernateTemplate().find(hqlQuery);
        if(list.isEmpty()){
            return false;
        }
        if(list.get(0)==1){
            return true;
        }else{
            return false;
        }

    }

    
    
    
    
}
