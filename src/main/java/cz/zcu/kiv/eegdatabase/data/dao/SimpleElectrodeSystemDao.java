package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeSystem;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class SimpleElectrodeSystemDao extends SimpleGenericDao<ElectrodeSystem, Integer>
        implements GenericListDaoWithDefault<ElectrodeSystem> {

    public SimpleElectrodeSystemDao() {
        super(ElectrodeSystem.class);
    }

    @Override
    public void createGroupRel(ElectrodeSystem persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getElectrodeSystems().add(persistent);
    }

    @Override
    public List<ElectrodeSystem> getItemsForList() {
        String hqlQuery = "from ElectrodeSystem el order by el.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ElectrodeSystem> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ElectrodeSystem el inner join fetch el.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select el.electrodeConfs from ElectrodeSystem el where el.electrodeSystemId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeSystem> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ElectrodeSystem el where el.electrodeSystemId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeSystem> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ElectrodeSystem persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getElectrodeSystems().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ElectrodeSystem persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ElectrodeSystem> getDefaultRecords() {
        String hqlQuery = "from ElectrodeSystem el where el.defaultNumber=1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select el.defaultNumber from ElectrodeSystem el where el.electrodeSystemId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
