package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairmentGroupRel;

import java.util.List;

public class SimpleVisualImpairmentDao extends SimpleGenericDao<VisualImpairment, Integer> implements VisualImpairmentDao {
    public SimpleVisualImpairmentDao() {
        super(VisualImpairment.class);
    }

    public List<VisualImpairment> getItemsForList() {
        String hqlQuery = "from VisualImpairment i order by i.description";
        List<VisualImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }


    //TODO odstranit s wizardem
    public boolean canSaveDescription(String description, int id) {
        String hqlQuery = "from VisualImpairment i where i.description = :description and i.visualImpairmentId != :id";
        String[] names = {"description", "id"};
        Object[] values = {description, id};
        List<VisualImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }


    public boolean canSaveDescription(String description, int groupId, int visualImpairmentId) {
        String hqlQuery = "from VisualImpairment h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" and h.description=\'" + description + "\' and h.visualImpairmentId<>"+visualImpairmentId+" ";
        List<VisualImpairment> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select i.persons from VisualImpairment i where i.visualImpairmentId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<VisualImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
    
    public List<VisualImpairment> getRecordsByGroup(int groupId){
        String hqlQuery = "from VisualImpairment h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        List<VisualImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    public void createDefaultRecord(VisualImpairment visualImpairment){
        visualImpairment.setDefaultNumber(1);
        create(visualImpairment);
    }

    public List<VisualImpairment> getDefaultRecords(){
        String hqlQuery = "from VisualImpairment h where h.defaultNumber=1";
        List<VisualImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    
    /**
    * Description of visualImpairment must be unique in a research group or between default
    *
    * @param description - visualImpairment description
    * @return
    */
   public boolean canSaveDefaultDescription(String description, int visualImpairmentId) {
       String hqlQuery = "from VisualImpairment h where h.description = :description and h.defaultNumber=1 and h.visualImpairmentId<>"+visualImpairmentId+" ";
       String name = "description";
       Object value = description;
       List<VisualImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, name, value);
       return (list.size() == 0);
   }




    public boolean hasGroupRel(int id){
        String hqlQuery = "from VisualImpairmentGroupRel r where r.id.visualImpairmentId =" +id+ " ";
        List<VisualImpairmentGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() > 0);
    }

    public void deleteGroupRel(VisualImpairmentGroupRel visualImpairmentGroupRel){
        getHibernateTemplate().delete(visualImpairmentGroupRel);
    }

    public VisualImpairmentGroupRel getGroupRel(int visualImpairmentId, int researchGroupId){
        String hqlQuery = "from VisualImpairmentGroupRel r where r.id.visualImpairmentId="+visualImpairmentId+" and r.id.researchGroupId="+researchGroupId+" ";
        List<VisualImpairmentGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return list.get(0);
    }

    public void createGroupRel(VisualImpairmentGroupRel visualImpairmentGroupRel){
        visualImpairmentGroupRel.getVisualImpairment().setDefaultNumber(0);
        getHibernateTemplate().save(visualImpairmentGroupRel);
    }

    public boolean isDefault(int id){
        String hqlQuery = "select h.defaultNumber from VisualImpairment h where h.visualImpairmentId="+id+" ";
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
