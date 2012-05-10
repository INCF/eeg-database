package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class SimplePharmaceuticalDao extends SimpleGenericDao<Pharmaceutical, Integer>
        implements GenericListDao<Pharmaceutical>{

    public SimplePharmaceuticalDao() {
        super(Pharmaceutical.class);
    }
    @Override
    public void createGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getPharmaceuticals().add(persistent);
    }

    @Override
    public List<Pharmaceutical> getItemsForList() {
        String hqlQuery = "from Pharmaceutical ph order by ph.title";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<Pharmaceutical> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Pharmaceutical ph inner join fetch ph.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        return getHibernateTemplate().find(hqlQuery);

    }

    @Override
    public boolean canDelete(int id) {
       String hqlQuery = "select ph.experiments from Pharmaceutical ph where ph.pharmaceuticalId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Pharmaceutical> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Pharmaceutical ph where ph.pharmaceuticalId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Pharmaceutical> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getPharmaceuticals().remove(persistent);
    }

}
