package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
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

    public boolean canDelete(int id) {
        String hqlQuery = "select w.experiments from Weather w where w.id = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    //TODO odstranit s wizardem
    public boolean canSaveNewDescription(String description) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where h.description=\'" + description + "\'";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

    public boolean canSaveNewDescription(String description, int groupId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" and h.description=\'" + description + "\'";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

    public boolean canSaveDescription(String description, int groupId, int weatherId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" and h.description=\'" + description + "\' and h.weatherId<>"+weatherId+" ";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

    public List<Weather> getRecordsNewerThan(long oracleScn) {
      String hqlQuery = "from Weather w where w.scn > :oracleScn";
       String name = "oracleScn";
       Object value = oracleScn;
      return getHibernateTemplate().findByNamedParam(hqlQuery, name, value);
    }

    public List<Weather> getRecordsByGroup(int groupId){
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" ";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }
    
    public void createDefaultRecord(Weather
        weather){
        weather.setDefaultNumber(1);
        create(weather);
    }

    public List<Weather> getDefaultRecords(){
        String hqlQuery = "from Weather h where h.defaultNumber=1";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canSaveTitle(String title, int groupId, int weatherId) {
        String hqlQuery = "from Weather h inner join fetch h.researchGroups as rg where rg.researchGroupId="+groupId+" and h.title=\'" + title + "\' and h.weatherId<>"+weatherId+" ";
        List<Weather> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() == 0);
    }

     /**
     * Title of weather must be unique in a research group or between default
     *
     * @param title - weather title
     * @return
     */
    public boolean canSaveDefaultTitle(String title, int weatherId) {
        String hqlQuery = "from Weather h where h.title = :title and h.defaultNumber=1 and h.weatherId<>"+weatherId+" ";
        String name = "title";
        Object value = title;
        List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, name, value);
        return (list.size() == 0);
    }

    /**
    * Description of weather must be unique in a research group or between default
    *
    * @param description - weather description
    * @return
    */
   public boolean canSaveDefaultDescription(String description, int weatherId) {
       String hqlQuery = "from Weather h where h.description = :description and h.defaultNumber=1 and h.weatherId<>"+weatherId+" ";
       String name = "description";
       Object value = description;
       List<Weather> list = getHibernateTemplate().findByNamedParam(hqlQuery, name, value);
       return (list.size() == 0);
   }




    public boolean hasGroupRel(int id){
        String hqlQuery = "from WeatherGroupRel r where r.id.weatherId =" +id+ " ";
        List<WeatherGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return (list.size() > 0);
    }

    public void deleteGroupRel(WeatherGroupRel
        weatherGroupRel){
        getHibernateTemplate().delete(weatherGroupRel);
    }

    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId){
        String hqlQuery = "from WeatherGroupRel r where r.id.weatherId="+weatherId+" and r.id.researchGroupId="+researchGroupId+" ";
        List<WeatherGroupRel> list = getHibernateTemplate().find(hqlQuery);
        return list.get(0);
    }

    public void createGroupRel(WeatherGroupRel
        weatherGroupRel){
        weatherGroupRel.getWeather().setDefaultNumber(0);
        getHibernateTemplate().save(weatherGroupRel);
    }

    public boolean isDefault(int id){
        String hqlQuery = "select h.defaultNumber from Weather h where h.weatherId="+id+" ";
        List<Integer> list = getHibernateTemplate().find(hqlQuery);
        if(list.isEmpty()){
            return false;
        }
        if(list.get(0)==1){
            return true;
        }else{
            return false;
        }

    }

    
    
}
