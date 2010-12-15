package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;

import java.util.List;

public class SimpleHardwareDao extends SimpleGenericDao<Hardware, Integer> implements HardwareDao {
    public SimpleHardwareDao() {
        super(Hardware.class);
    }

    public List<Hardware> getItemsForList() {
        String hqlQuery = "from Hardware h order by h.title, h.type";
        List<Hardware> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
