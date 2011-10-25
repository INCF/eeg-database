package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;

import java.util.List;

public interface WeatherDao extends GenericDao<Weather, Integer> {
    public List<Weather> getItemsForList();

    public List<Weather> getRecordsNewerThan(long oracleScn);

    public boolean canSaveTitle(String title, int id);

    public boolean canSaveDescription(String description, int id);

    public boolean canDelete(int id);

    /**
     * Description of weather must be unique
     * @param description - description of weather
     * @return
     */
    public boolean canSaveNewDescription(String description);
}
