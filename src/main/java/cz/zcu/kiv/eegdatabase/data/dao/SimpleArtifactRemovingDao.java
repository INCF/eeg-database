package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ArtifactRemoveMethod;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class SimpleArtifactRemovingDao extends SimpleGenericDao<ArtifactRemoveMethod, Integer>
        implements GenericListDaoWithDefault<ArtifactRemoveMethod> {

    public SimpleArtifactRemovingDao() {
        super(ArtifactRemoveMethod.class);
    }

    @Override
    public void createGroupRel(ArtifactRemoveMethod persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getArtifactRemoveMethods().add(persistent);
    }

    @Override
    public List<ArtifactRemoveMethod> getItemsForList() {
        String hqlQuery = "from ArtifactRemoveMethod ar order by ar.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ArtifactRemoveMethod> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ArtifactRemoveMethod ar inner join fetch ar.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ar.experiments from ArtifactRemoveMethod ar where ar.artifactRemoveMethodId = :id";
        List<ArtifactRemoveMethod> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ArtifactRemoveMethod ar where ar.artifactRemoveMethodId = :id";
        List<ArtifactRemoveMethod> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ArtifactRemoveMethod persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getArtifactRemoveMethods().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ArtifactRemoveMethod persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ArtifactRemoveMethod> getDefaultRecords() {
        String hqlQuery = "from ArtifactRemoveMethod ar where ar.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select ar.defaultNumber from ArtifactRemoveMethod ar where ar.educationLevelId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
