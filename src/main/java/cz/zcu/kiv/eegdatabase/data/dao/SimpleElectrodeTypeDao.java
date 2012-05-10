package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class SimpleElectrodeTypeDao extends SimpleGenericDao<ElectrodeType, Integer>
        implements GenericListDaoWithDefault<ElectrodeType>{

        public SimpleElectrodeTypeDao() {
        super(ElectrodeType.class);
    }

    @Override
    public void createGroupRel(ElectrodeType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getElectrodeTypes().add(persistent);
    }

    @Override
    public List<ElectrodeType> getItemsForList() {
        String hqlQuery = "from ElectrodeType el order by el.title";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<ElectrodeType> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ElectrodeType el inner join fetch el.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        return getHibernateTemplate().find(hqlQuery);

    }

    @Override
    public boolean canDelete(int id) {
       String hqlQuery = "select el.electrodeLocations from ElectrodeType el where el.electrodeTypeId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ElectrodeType el where el.electrodeFixId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ElectrodeType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ElectrodeType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getElectrodeTypes().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ElectrodeType persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ElectrodeType> getDefaultRecords() {
        String hqlQuery = "from ElectrodeType el where el.defaultNumber=1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
       String hqlQuery = "select el.defaultNumber from ElectrodeType el where el.electrodeTypeId="+id+" ";
        List<Integer> list = getHibernateTemplate().find(hqlQuery);
        if(list.isEmpty()){
            return false;
        }
        return (list.get(0)==1);

    }
}
