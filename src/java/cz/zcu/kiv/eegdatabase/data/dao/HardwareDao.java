package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;

import java.util.List;

public interface HardwareDao extends GenericDao<Hardware, Integer> {
    public List<Hardware> getItemsForList();
}
