package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;

import java.util.List;

public interface WeatherDao extends GenericDao<Weather, Integer> {
    public List<Weather> getItemsForList();

    public boolean canSaveTitle(String title, int id);

    public boolean canSaveDescription(String description, int id);
}
