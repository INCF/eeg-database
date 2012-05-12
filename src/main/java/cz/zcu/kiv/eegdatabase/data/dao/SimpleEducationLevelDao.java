package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * This class extends powers class SimpleGenericDao.
 * Class is determined only for EducationLevel.
 *
 * @author Jiri Novotny
 */
public class SimpleEducationLevelDao
        extends SimpleGenericDao<EducationLevel, Integer> implements EducationLevelDao {

    public SimpleEducationLevelDao() {
        super(EducationLevel.class);
    }

    /**
     * Finds all education levels with the specified title
     *
     * @param title - title property value
     * @return List of EducationLevel entities with searched title
     */
    public List<EducationLevel> getEducationLevels(String title) {
        String HQLselect = "from EducationLevel level where level.title = :title";
        return getHibernateTemplate().findByNamedParam(HQLselect, "title", title);
    }

    @Override
    public void createGroupRel(EducationLevel persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getEducationLevels().add(persistent);
    }

    @Override
    public List<EducationLevel> getItemsForList() {
        String hqlQuery = "from EducationLevel ed order by ed.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<EducationLevel> getRecordsByGroup(int groupId) {
        String hqlQuery = "from EducationLevel ed inner join fetch ed.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ed.persons from EducationLevel ed where ed.educationLevelId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<EducationLevel> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from EducationLevel ed where ed.educationLevelId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<EducationLevel> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(EducationLevel persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getEducationLevels().remove(persistent);
    }

    @Override
    public void createDefaultRecord(EducationLevel educationLevel) {
        educationLevel.setDefaultNumber(1);
        create(educationLevel);
    }

    @Override
    public List<EducationLevel> getDefaultRecords() {
        String hqlQuery = "from EducationLevel ed where ed.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select ed.defaultNumber from EducationLevel ed where ed.educationLevelId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
