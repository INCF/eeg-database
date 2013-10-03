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
 *   WeatherServicePerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
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
        weatherDao.create(weather);
    }


/**
 * Method test create Wheather.
 * Identificator of test / PPT_LWD_19_AddWd_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateWeatherTest(){
      int countRecord = weatherDao.getCountRecords();

       createTestWheather();


       assertEquals(weatherDao.getCountRecords()-1, countRecord);

    }

/**
 * Method test edit Wheathert.
 * Identificator of test / PPT_LWD_20_EdiWd_F/ /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditWeatherTest(){
        createTestWheather();
        List<Weather> listRecords;

        weather.setDescription(WEATHER_DESCRIPTION+"EDITOAVANY");
        weatherDao.update(weather);

        listRecords=weatherDao.getAllRecords();
        assertEquals(weatherDao.read(weather.getWeatherId()).getDescription(), weather.getDescription());


    }
/**
 * Method test delete Wheather.
 * Identificator of test / PPT_LWD_21_DelWd_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeleteWeatherTest(){
        createTestWheather();
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
