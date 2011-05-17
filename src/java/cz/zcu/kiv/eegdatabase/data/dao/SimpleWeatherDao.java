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

    public boolean canSaveTitle(String title, int id) {
        String hqlQuery = "from Weather w where w.title = :title and w.weatherId != :id";
        String[] names = {"title", "id"};
        Object[] values = {title, id};
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public boolean canSaveDescription(String description, int id) {
        String hqlQuery = "from Weather w where w.description = :description and w.weatherId != :id";
        String[] names = {"description", "id"};
        Object[] values = {description, id};
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select w.experiments from Weather w where w.id = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }
}
