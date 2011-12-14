package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;

import java.util.List;

public class SimpleExperimentOptParamDefDao extends SimpleGenericDao<ExperimentOptParamDef, Integer> implements ExperimentOptParamDefDao {
    public SimpleExperimentOptParamDefDao() {
        super(ExperimentOptParamDef.class);
    }

    public List<ExperimentOptParamDef> getItemsForList() {
        String hqlQuery = "from ExperimentOptParamDef i order by i.paramName";
        List<ExperimentOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select def.experimentOptParamVals from ExperimentOptParamDef def where def.experimentOptParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ExperimentOptParamDef> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
      
    public List<ExperimentOptParamDef> getRecordsByGroup(int groupId){
        String hqlQuery = "from ExperimentOptParamDef h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        List<ExperimentOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef){
        experimentOptParamDef.setDefaultNumber(1);
        create(experimentOptParamDef);
    }

    public List<ExperimentOptParamDef> getDefaultRecords(){
        String hqlQuery = "from ExperimentOptParamDef h where h.defaultNumber=1";
        List<ExperimentOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean hasGroupRel(int id){
        String hqlQuery = "from ExperimentOptParamDefGroupRel r where r.id.experimentOptParamDefId =" +id+ " ";
        List<ExperimentOptParamDefGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() > 0);
    }

    public void deleteGroupRel(ExperimentOptParamDefGroupRel
        experimentOptParamDefGroupRel){
        getHibernateTemplate().delete(experimentOptParamDefGroupRel);
    }

    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId){
        String hqlQuery = "from ExperimentOptParamDefGroupRel r where r.id.experimentOptParamDefId="+experimentOptParamDefId+" and r.id.researchGroupId="+researchGroupId+" ";
        List<ExperimentOptParamDefGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return list.get(0);
    }

    public void createGroupRel(ExperimentOptParamDefGroupRel
        experimentOptParamDefGroupRel){
        experimentOptParamDefGroupRel.getExperimentOptParamDef().setDefaultNumber(0);
        getHibernateTemplate().save(experimentOptParamDefGroupRel);
    }

    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup){
        experimentOptParamDef.getResearchGroups().add(researchGroup);
        researchGroup.getExperimentOptParamDefs().add(experimentOptParamDef);
    }    

    public boolean isDefault(int id){
        String hqlQuery = "select h.defaultNumber from ExperimentOptParamDef h where h.experimentOptParamDefId="+id+" ";
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
