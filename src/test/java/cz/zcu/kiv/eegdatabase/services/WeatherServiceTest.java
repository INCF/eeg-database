/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * WeatherServiceTest.java, 2014/08/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Honza on 7.8.14.
 */
public class WeatherServiceTest extends AbstractServicesTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Weather weather;
    private ResearchGroup researchGroup;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);
        weather = createWeather("new Weather");
    }

    @Test(groups = "unit")
    public void testCreateWeather() {
        int weatherCountBefore = weatherService.getAllRecords().size();
        int weatherID = weatherService.create(weather);
        assertEquals(weatherCountBefore + 1, weatherService.getAllRecords().size());
        assertEquals(weatherID, weather.getWeatherId());
    }

    @Test(groups = "unit")
    public void testCreateDefaultWeather() {
        int weatherCountBefore = weatherService.getAllRecords().size();
        weather.setDefaultNumber(1);
        weatherService.createDefaultRecord(weather);
        assertEquals(weatherCountBefore + 1, weatherService.getAllRecords().size());

        assertTrue(weatherService.isDefault(weather.getWeatherId()));
    }
    @Test(groups = "unit")
    public void testCreateWeatherGroupRel() {
        int weatherCountBefore = weatherService.getAllRecords().size();
        int weatherGroupBefore = weatherService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        weatherService.createGroupRel(weather, researchGroup);
        int id = weatherService.create(weather);
        assertEquals(weatherCountBefore + 1, weatherService.getAllRecords().size());

        List<Weather> list = weatherService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(weatherGroupBefore + 1, list.size());
        assertTrue(weatherService.hasGroupRel(id));
    }

    @Test(groups = "unit")
    public void testGetDefaultRecords() {
        int weatherCountBefore = weatherService.getAllRecords().size();
        int weatherDefaultBefore = weatherService.getDefaultRecords().size();
        //This is not a default Weather
        int id = weatherService.create(weather);

        Weather newWeather = createWeather("new weather2");
        newWeather.setDefaultNumber(1);
        weatherService.createDefaultRecord(newWeather);
        assertEquals(weatherCountBefore + 2, weatherService.getAllRecords().size());

        assertEquals(weatherDefaultBefore + 1, weatherService.getDefaultRecords().size());

        assertFalse(weatherService.hasGroupRel(newWeather.getWeatherId()));
    }

    @Test(groups = "unit")
    public void testCanSaveTitle() {
        weatherService.createGroupRel(weather, researchGroup);
        int id = weatherService.create(weather);
        assertTrue(weatherService.canSaveDefaultTitle(weather.getTitle(), -1));
        assertTrue(weatherService.canSaveTitle(weather.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(weatherService.canSaveTitle(weather.getTitle(), researchGroup.getResearchGroupId(), -1));

        Weather newWeather = createWeather("new Weather2");
        newWeather.setDefaultNumber(1);
        weatherService.createDefaultRecord(newWeather);
        assertFalse(weatherService.canSaveDefaultTitle(newWeather.getTitle(), -1));
        assertTrue(weatherService.canSaveDefaultTitle(newWeather.getTitle(), newWeather.getWeatherId()));
    }

    private Weather createWeather(String title) {
        Weather newWeather = new Weather();
        newWeather.setTitle(title);
        newWeather.setDescription("This is new testing Weather");
        newWeather.setDefaultNumber(0);
        return newWeather;
    }
}
