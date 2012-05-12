package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public class SimpleHardwareDao extends SimpleGenericDao<Hardware, Integer> implements HardwareDao {
    public SimpleHardwareDao() {
        super(Hardware.class);
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select h.experiments from Hardware h where h.hardwareId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Hardware> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public List<Hardware> getItemsForList() {
        String hqlQuery = "from Hardware h order by h.title, h.type";
        List<Hardware> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public List<Hardware> getRecordsNewerThan(long oracleScn) {
        String hqlQuery = "from Hardware h where h.scn > :oracleScn";
        List<Hardware> list = getHibernateTemplate().findByNamedParam(hqlQuery, "oracleScn", oracleScn);
        return list;
    }

    public List<Hardware> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Hardware h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId";
        List<Hardware> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        return list;
    }

    public void createDefaultRecord(Hardware hardware) {
        hardware.setDefaultNumber(1);
        create(hardware);
    }

    public List<Hardware> getDefaultRecords() {
        String hqlQuery = "from Hardware h where h.defaultNumber = 1";
        List<Hardware> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canSaveTitle(String title, int groupId, int hwId) {
        String hqlQuery = "from Hardware h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.title = :title and h.hardwareId <> :hwId";
        List<Hardware> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("title", title)
                .setParameter("hwId", hwId)
                .list();
        return (list.size() == 0);
    }

    public boolean canSaveNewTitle(String title, int groupId) {
        String hqlQuery = "from Hardware h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.title = :title";
        List<Hardware> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("title", title)
                .list();
        return (list.size() == 0);
    }


    /**
     * Title of hardware must be unique in a research group or between default
     *
     * @param title - hardware title
     * @return
     */
    public boolean canSaveDefaultTitle(String title, int hwId) {
        String hqlQuery = "from Hardware h where h.title = :title and h.defaultNumber = 1 and h.hardwareId <> :hwId";
        List<Hardware> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("hwId", hwId)
                .list();
        return (list.size() == 0);
    }


    public boolean hasGroupRel(int id) {
        String hqlQuery = "from HardwareGroupRel r where r.id.hardwareId = :id";
        List<HardwareGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() > 0);
    }

    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel) {
        getHibernateTemplate().delete(hardwareGroupRel);
    }

    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId) {
        String hqlQuery = "from HardwareGroupRel r where r.id.hardwareId = :hardwareId and r.id.researchGroupId = :researchGroupId";
        List<HardwareGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("hardwareId", hardwareId)
                .setParameter("researchGroupId", researchGroupId)
                .list();
        return list.get(0);
    }

    public void createGroupRel(HardwareGroupRel hardwareGroupRel) {
        hardwareGroupRel.getHardware().setDefaultNumber(0);
        getHibernateTemplate().save(hardwareGroupRel);
    }

    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup) {
        hardware.getResearchGroups().add(researchGroup);
        researchGroup.getHardwares().add(hardware);
    }

    public boolean isDefault(int id) {
        String hqlQuery = "select h.defaultNumber from Hardware h where h.hardwareId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        if (list.get(0) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
