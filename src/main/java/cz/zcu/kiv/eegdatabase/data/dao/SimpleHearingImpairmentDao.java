package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairmentGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;

import java.util.List;

public class SimpleHearingImpairmentDao extends SimpleGenericDao<HearingImpairment, Integer> implements HearingImpairmentDao {
    public SimpleHearingImpairmentDao() {
        super(HearingImpairment.class);
    }

    public List<HearingImpairment> getItemsForList() {
        String hqlQuery = "from HearingImpairment i order by i.description";
        List<HearingImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canSaveDescription(String description, int groupId, int hearingImpairmentId) {
        String hqlQuery = "from HearingImpairment h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" and h.description=\'" + description + "\' and h.hearingImpairmentId<>"+hearingImpairmentId+" ";
        List<HearingImpairment> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select i.persons from HearingImpairment i where i.hearingImpairmentId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<HearingImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
    
    public List<HearingImpairment> getRecordsByGroup(int groupId){
        String hqlQuery = "from HearingImpairment h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        List<HearingImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    public void createDefaultRecord(HearingImpairment hearingImpairment){
        hearingImpairment.setDefaultNumber(1);
        create(hearingImpairment);
    }

    public List<HearingImpairment> getDefaultRecords(){
        String hqlQuery = "from HearingImpairment h where h.defaultNumber=1";
        List<HearingImpairment> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    
    /**
    * Description of hearingImpairment must be unique in a research group or between default
    *
    * @param description - hearingImpairment description
    * @return
    */
   public boolean canSaveDefaultDescription(String description, int hearingImpairmentId) {
       String hqlQuery = "from HearingImpairment h where h.description = :description and h.defaultNumber=1 and h.hearingImpairmentId<>"+hearingImpairmentId+" ";
       String name = "description";
       Object value = description;
       List<HearingImpairment> list = getHibernateTemplate().findByNamedParam(hqlQuery, name, value);
       return (list.size() == 0);
   }




    public boolean hasGroupRel(int id){
        String hqlQuery = "from HearingImpairmentGroupRel r where r.id.hearingImpairmentId =" +id+ " ";
        List<HearingImpairmentGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() > 0);
    }

    public void deleteGroupRel(HearingImpairmentGroupRel hearingImpairmentGroupRel){
        getHibernateTemplate().delete(hearingImpairmentGroupRel);
    }

    public HearingImpairmentGroupRel getGroupRel(int hearingImpairmentId, int researchGroupId){
        String hqlQuery = "from HearingImpairmentGroupRel r where r.id.hearingImpairmentId="+hearingImpairmentId+" and r.id.researchGroupId="+researchGroupId+" ";
        List<HearingImpairmentGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return list.get(0);
    }

    public void createGroupRel(HearingImpairmentGroupRel hearingImpairmentGroupRel){
        hearingImpairmentGroupRel.getHearingImpairment().setDefaultNumber(0);
        getHibernateTemplate().save(hearingImpairmentGroupRel);
    }

    public void createGroupRel(HearingImpairment hearingImpairment, ResearchGroup researchGroup){
        hearingImpairment.getResearchGroups().add(researchGroup);
        researchGroup.getHearingImpairments().add(hearingImpairment);
    }    

    public boolean isDefault(int id){
        String hqlQuery = "select h.defaultNumber from HearingImpairment h where h.hearingImpairmentId="+id+" ";
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
