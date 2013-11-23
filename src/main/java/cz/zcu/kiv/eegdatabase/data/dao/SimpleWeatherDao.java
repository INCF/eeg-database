/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SimpleWeatherDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;

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

    public boolean canDelete(int id) {
        String hqlQuery = "select w.experiments from Weather w where w.id = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public boolean canSaveNewDescription(String description, int groupId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.description = :description";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("description", description)
                .list();
        return (list.size() == 0);
    }

    public boolean canSaveDescription(String description, int groupId, int weatherId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.description = :description and h.weatherId <> :weatherId";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("description", description)
                .setParameter("weatherId", weatherId)
                .list();
        return (list.size() == 0);
    }

    public List<Weather> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        return list;
    }

    public void createDefaultRecord(Weather weather) {
        weather.setDefaultNumber(1);
        create(weather);
    }

    public List<Weather> getDefaultRecords() {
        String hqlQuery = "from Weather h where h.defaultNumber = 1";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canSaveTitle(String title, int groupId, int weatherId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.title = :title and h.weatherId <> :weatherId";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("title", title)
                .setParameter("weatherId", weatherId)
                .list();
        return (list.size() == 0);
    }

    public boolean canSaveNewTitle(String title, int groupId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId and h.title = :title";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("groupId", groupId)
                .setParameter("title", title)
                .list();
        return (list.size() == 0);
    }


    /**
     * Title of weather must be unique in a research group or between default
     *
     * @param title - weather title
     * @return
     */
    public boolean canSaveDefaultTitle(String title, int weatherId) {
        String hqlQuery = "from Weather h where h.title = :title and h.defaultNumber = 1 and h.weatherId <> :weatherId";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("weatherId", weatherId)
                .list();
        return (list.size() == 0);
    }

    /**
     * Description of weather must be unique in a research group or between default
     *
     * @param description - weather description
     * @return
     */
    public boolean canSaveDefaultDescription(String description, int weatherId) {
        String hqlQuery = "from Weather h where h.description = :description and h.defaultNumber = 1 and h.weatherId <> :weatherId";
        List<Weather> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("description", description)
                .setParameter("weatherId", weatherId)
                .list();
        return (list.size() == 0);
    }


    public boolean hasGroupRel(int id) {
        String hqlQuery = "from WeatherGroupRel r where r.id.weatherId = :id";
        List<WeatherGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() > 0);
    }

    public void deleteGroupRel(WeatherGroupRel weatherGroupRel) {
        getHibernateTemplate().delete(weatherGroupRel);
    }

    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId) {
        String hqlQuery = "from WeatherGroupRel r where r.id.weatherId = :weatherId and r.id.researchGroupId = :researchGroupId";
        List<WeatherGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("weatherId", weatherId)
                .setParameter("researchGroupId", researchGroupId)
                .list();
        return list.get(0);
    }

    public void createGroupRel(WeatherGroupRel weatherGroupRel) {
        weatherGroupRel.getWeather().setDefaultNumber(0);
        getHibernateTemplate().save(weatherGroupRel);
    }

    public void createGroupRel(Weather weather, ResearchGroup researchGroup) {
        weather.getResearchGroups().add(researchGroup);
        researchGroup.getWeathers().add(weather);
    }

    public boolean isDefault(int id) {
        String hqlQuery = "select h.defaultNumber from Weather h where h.weatherId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        if (list.get(0) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
