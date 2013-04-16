package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class SimpleSoftwareDao extends SimpleGenericDao<Software, Integer>
        implements GenericListDaoWithDefault<Software> {

    public SimpleSoftwareDao() {
        super(Software.class);
    }

    @Override
    public void createGroupRel(Software persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getSoftwares().add(persistent);
    }

    @Override
    public List<Software> getItemsForList() {
        String hqlQuery = "from Software st order by st.description";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Software> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Software st inner join fetch st.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select st.experiments from Software st where st.softwareId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Software> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Software st where st.softwareId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Software> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Software persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getSoftwares().remove(persistent);
    }

    @Override
    public void createDefaultRecord(Software persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<Software> getDefaultRecords() {
        String hqlQuery = "from Software st where st.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select st.defaultNumber from Software st where st.softwareId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }

    public boolean canSaveDefaultTitle(String title, int swId) {
        String hqlQuery = "from Software s where s.title = :title and s.defaultNumber = 1 and s.softwareId <> :swId";
        List<Software> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("swId", swId)
                .list();
        return (list.size() == 0);
    }
}
