package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;

import java.util.List;

public interface HardwareDao extends GenericDao<Hardware, Integer> {
    public void createDefaultRecord(Hardware hardware);

    public void createGroupRecord(HardwareGroupRel hardwareGroupRel);

    public List<Hardware> getItemsForList();

    public List<Hardware> getRecordsNewerThan(long oracleScn);

    public List<Hardware> getRecordsByGroup(int groupId);

    public List<Hardware> getDefaultRecords();

    public boolean canSaveTitle(String title, int id);

    public boolean canDelete(int id);

    /**
     * Title of hardware must be unique
     * @param title
     * @return
     */
    public boolean canSaveTitle(String title);

}
