package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface HardwareDao extends GenericDao<Hardware, Integer> {
    public void createDefaultRecord(Hardware hardware);

    public void createGroupRel(HardwareGroupRel hardwareGroupRel);

    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup);

    public List<Hardware> getItemsForList();

    public List<Hardware> getRecordsNewerThan(long oracleScn);

    public List<Hardware> getRecordsByGroup(int groupId);

    public List<Hardware> getDefaultRecords();

    public boolean canDelete(int id);

    public boolean isDefault(int id);

    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel);

    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId);

    public boolean hasGroupRel(int id);

    public boolean canSaveTitle(String title, int groupId, int hwId);

    public boolean canSaveNewTitle(String title, int groupId);

    public boolean canSaveDefaultTitle(String title, int hwId);

}
