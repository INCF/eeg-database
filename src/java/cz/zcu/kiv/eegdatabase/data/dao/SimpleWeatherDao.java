package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;

import java.util.List;

public class SimpleWeatherDao extends SimpleGenericDao<Weather, Integer> implements WeatherDao {
    public SimpleWeatherDao() {
        super(Weather.class);
    }

    public List<Weather> getItemsForList() {
        String hqlQuery = "from Weather w order by w.title";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
}
