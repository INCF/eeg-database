package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface WeatherService extends GenericService<Weather, Integer> {

    List<Weather> getItemsForList();

    List<Weather> getRecordsNewerThan(long oracleScn);

    boolean canSaveDescription(String description, int groupId, int weatherId);

    boolean canSaveDefaultDescription(String description, int weatherId);

    boolean canDelete(int id);

    boolean canSaveNewDescription(String description, int groupId);

    void createDefaultRecord(Weather weather);

    void createGroupRel(WeatherGroupRel weatherGroupRel);

    void createGroupRel(Weather weather, ResearchGroup researchGroup);

    List<Weather> getRecordsByGroup(int groupId);

    List<Weather> getDefaultRecords();

    boolean isDefault(int id);

    void deleteGroupRel(WeatherGroupRel weatherGroupRel);

    WeatherGroupRel getGroupRel(int weatherId, int researchGroupId);

    boolean hasGroupRel(int id);

    boolean canSaveTitle(String title, int groupId, int weatherId);

    boolean canSaveNewTitle(String title, int groupId);

    boolean canSaveDefaultTitle(String title, int weatherId);

}
