package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDiseaseDao extends SimpleGenericDao<Disease, Integer>
        implements GenericListDao<Disease>{

    public SimpleDiseaseDao() {
        super(Disease.class);
    }
    @Override
    public void createGroupRel(Disease persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getDiseases().add(persistent);
    }

    @Override
    public List<Disease> getItemsForList() {
        String hqlQuery = "from Disease dis order by dis.title";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<Disease> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Disease dis inner join fetch dis.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        return getHibernateTemplate().find(hqlQuery);

    }

    @Override
    public boolean canDelete(int id) {
       String hqlQuery = "select dis.experiments from Disease dis where dis.diseaseId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Disease> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Disease dis where dis.diseaseId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Disease> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Disease persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getDiseases().remove(persistent);
    }

}
