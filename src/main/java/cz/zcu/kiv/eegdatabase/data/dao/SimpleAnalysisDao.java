package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Analysis;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class SimpleAnalysisDao extends SimpleGenericDao<Analysis, Integer>
        implements GenericListDao<Analysis> {

    public SimpleAnalysisDao() {
        super(Analysis.class);
    }
    @Override
    public void createGroupRel(Analysis persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getAnalyses().add(persistent);
    }

    @Override
    public List<Analysis> getItemsForList() {
        String hqlQuery = "from Analysis an order by an.description";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<Analysis> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Analysis an inner join fetch an.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        return getHibernateTemplate().find(hqlQuery);

    }

    @Override
    public boolean canDelete(int id) {
       String hqlQuery = "select an.dataFiles from Analysis an where an.analysisId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Analysis> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from EducationLevel ed where ed.educationLevelId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Analysis> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Analysis persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getAnalyses().remove(persistent);
    }

}
