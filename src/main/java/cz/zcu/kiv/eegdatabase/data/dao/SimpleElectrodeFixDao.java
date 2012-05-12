package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeFix;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class SimpleElectrodeFixDao extends SimpleGenericDao<ElectrodeFix, Integer>
        implements GenericListDaoWithDefault<ElectrodeFix> {

    public SimpleElectrodeFixDao() {
        super(ElectrodeFix.class);
    }

    @Override
    public void createGroupRel(ElectrodeFix persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getElectrodeFixes().add(persistent);
    }

    @Override
    public List<ElectrodeFix> getItemsForList() {
        String hqlQuery = "from ElectrodeFix el order by el.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ElectrodeFix> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ElectrodeFix el inner join fetch el.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select el.electrodeLocations from ElectrodeFix el where el.electrodeFixId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeFix> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ElectrodeFix el where el.electrodeFixId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeFix> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ElectrodeFix persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getElectrodeFixes().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ElectrodeFix persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ElectrodeFix> getDefaultRecords() {
        String hqlQuery = "from ElectrodeFix el where el.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select el.defaultNumber from ElectrodeFix el where el.electrodeFixId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
