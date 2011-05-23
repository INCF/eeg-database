package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LWD_18_WorWitWd_L/. Contains document Testovaci scenare.docx.
 */
public class WeatherServicePerformanceTest extends PerformanceTest {

   @Autowired
    PersonDao personeDao;
    @Autowired
    WeatherDao weatherDao;

    private Weather weather;


/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LWD_19_AddWd_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createWeatherTest(){
        weather = new Weather();
        weather.setDescription("popisTest");
        weather.setTitle("titleWeather");
        weatherDao.create(weather);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LWD_20_EdiWd_F/ /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editWeatherTest(){
        weather.setDescription("popisTest");
       weather.setTitle("titleWeather");
        weatherDao.update(weather);
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LWD_21_DelWd_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteWeatherTest(){
        weatherDao.delete(weather);
    }
}
