package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class SimpleArtifactDao extends SimpleGenericDao<Artifact, Integer>
        implements GenericListDao<Artifact> {

    public SimpleArtifactDao() {
        super(Artifact.class);
    }

    @Override
    public void createGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getArtifacts().add(persistent);
    }

    @Override
    public List<Artifact> getItemsForList() {
        String hqlQuery = "from Artifact ar order by ar.compensation";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Artifact> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Artifact ar inner join fetch ar.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ar.experiments from Artifact ar where ar.artifactId = :id";
        List<Artifact> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Artifact ar where ar.artifactId = :id";
        List<Artifact> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getArtifacts().remove(persistent);
    }
}
