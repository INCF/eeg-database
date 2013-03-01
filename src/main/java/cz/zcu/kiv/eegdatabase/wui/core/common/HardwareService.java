package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface HardwareService extends GenericService<Hardware, Integer> {

    void createDefaultRecord(Hardware hardware);

    void createGroupRel(HardwareGroupRel hardwareGroupRel);

    void createGroupRel(Hardware hardware, ResearchGroup researchGroup);

    List<Hardware> getItemsForList();

    List<Hardware> getRecordsNewerThan(long oracleScn);

    List<Hardware> getRecordsByGroup(int groupId);

    List<Hardware> getDefaultRecords();

    boolean canDelete(int id);

    boolean isDefault(int id);

    void deleteGroupRel(HardwareGroupRel hardwareGroupRel);

    HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId);

    boolean hasGroupRel(int id);

    boolean canSaveTitle(String title, int groupId, int hwId);

    boolean canSaveNewTitle(String title, int groupId);

    boolean canSaveDefaultTitle(String title, int hwId);
}
