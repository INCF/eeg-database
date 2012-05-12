package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeLocation;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class SimpleElectrodeLocationDao extends SimpleGenericDao<ElectrodeLocation, Integer>
        implements GenericListDaoWithDefault<ElectrodeLocation> {

    public SimpleElectrodeLocationDao() {
        super(ElectrodeLocation.class);
    }

    @Override
    public void createGroupRel(ElectrodeLocation persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getElectrodeLocations().add(persistent);
    }

    @Override
    public List<ElectrodeLocation> getItemsForList() {
        String hqlQuery = "from ElectrodeLocation el order by el.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ElectrodeLocation> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ElectrodeLocation el inner join fetch el.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select el.electrodeConfs from ElectrodeLocation el where el.electrodeLocationId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeLocation> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ElectrodeLocation el where el.electrodeLocationId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeLocation> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ElectrodeLocation persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getElectrodeLocations().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ElectrodeLocation persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ElectrodeLocation> getDefaultRecords() {
        String hqlQuery = "from ElectrodeLocation  el where el.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select el.defaultNumber from ElectrodeLocation el where el.electrodeLocationId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
