package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;

import java.util.List;

public interface WeatherDao extends GenericDao<Weather, Integer> {
    public List<Weather> getItemsForList();

    public List<Weather> getRecordsNewerThan(long oracleScn);

    public boolean canSaveDescription(String description, int groupId, int weatherId);

    public boolean canSaveDefaultDescription(String description, int weatherId);

    public boolean canDelete(int id);

    /**
     * Description of weather must be unique in a research group
     * @param description - description of weather
     * @return
     */
    public boolean canSaveNewDescription(String description, int groupId);
    //TODO upravit spolu s wizardem
    public boolean canSaveNewDescription(String description);

    public void createDefaultRecord(Weather weather);

    public void createGroupRel(WeatherGroupRel weatherGroupRel);

    public void createGroupRel(Weather weather, ResearchGroup researchGroup);

    public List<Weather> getRecordsByGroup(int groupId);

    public List<Weather> getDefaultRecords();

    public boolean isDefault(int id);

    public void deleteGroupRel(WeatherGroupRel weatherGroupRel);

    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId);

    public boolean hasGroupRel(int id);

    public boolean canSaveTitle(String title, int groupId, int weatherId);

    public boolean canSaveDefaultTitle(String title, int weatherId);
}
