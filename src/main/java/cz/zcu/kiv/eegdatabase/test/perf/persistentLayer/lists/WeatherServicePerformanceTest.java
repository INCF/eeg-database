package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import java.util.List;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LWD_18_WorWitWd_L/. Contains document Testovaci scenare.docx.
 */
public class WeatherServicePerformanceTest extends PerformanceTest {


    /**
     * Constant for atribute of test data.
     */

    public static final String WEATHER_DESCRIPTION = "mraky, slunce, dest";
    public static final String WEATHER_TITLE = "Stridavo";



    WeatherDao weatherDao;

    private Weather weather;



    /**
* Method test create wheather for next test.
*
*/

    public void createTestWheather(){
        weather = new Weather();
        weather.setDescription(WEATHER_DESCRIPTION);
        weather.setTitle(WEATHER_TITLE);
    }


/**
 * Method test create Wheather.
 * Identificator of test / PPT_LWD_19_AddWd_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateWeatherTest(){
      int countRecord = weatherDao.getCountRecords();

       createTestWheather();
       weatherDao.create(weather);


       assertEquals(weatherDao.getCountRecords()-1, countRecord);

    }

/**
 * Method test edit Wheathert.
 * Identificator of test / PPT_LWD_20_EdiWd_F/ /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditWeatherTest(){

        List<Weather> listRecords;

        weather.setDescription(WEATHER_DESCRIPTION+"EDITOAVANY");
        weatherDao.update(weather);

        listRecords=weatherDao.getAllRecords();
        assertEquals(listRecords.get(listRecords.size()-1).getDescription(), weather.getDescription());


    }
/**
 * Method test delete Wheather.
 * Identificator of test / PPT_LWD_21_DelWd_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeleteWeatherTest(){

        int countRecord = weatherDao.getCountRecords();

        weatherDao.delete(weather);

        assertEquals(weatherDao.getCountRecords()+1, countRecord);
    }

     /**
     * Setter for DAO object.
     */

     public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }
}
