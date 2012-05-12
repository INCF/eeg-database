package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProjectTypeDao extends SimpleGenericDao<ProjectType, Integer>
        implements GenericListDao<ProjectType> {

    public SimpleProjectTypeDao() {
        super(ProjectType.class);
    }

    @Override
    public void createGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getProjectTypes().add(persistent);
    }

    @Override
    public List<ProjectType> getItemsForList() {
        String hqlQuery = "from ProjectType pr order by pr.title";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<ProjectType> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ProjectType pr inner join fetch pr.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select pr.experiments from ProjectType pr where pr.projectTypeId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ProjectType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ProjectType pr where pr.projectTypeId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ProjectType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getProjectTypes().remove(persistent);
    }

}
