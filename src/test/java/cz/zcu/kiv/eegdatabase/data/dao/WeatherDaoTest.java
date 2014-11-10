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
 *   WeatherDaoTest.java, 2014/07/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Jan Stebetak
 * Date: 1.7.14
 */
public class WeatherDaoTest extends AbstractDataAccessTest {

    @Autowired
    private WeatherDao weatherDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    private Weather weather;

    private ResearchGroup researchGroup;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        weather = new Weather();
        weather.setTitle("Test-title");
        weather.setDescription("Test-description");
        weather.setDefaultNumber(0);

        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);
    }

    @Test(groups = "unit")
    public void testCreateWeather() throws Exception {
        int weatherCountBefore = weatherDao.getCountRecords();
        int weatherID = weatherDao.create(weather);
        assertEquals(weatherCountBefore + 1, weatherDao.getAllRecords().size());
        assertEquals(weatherID, weather.getWeatherId());
    }

    @Test(groups = "unit")
    public void testCreateDefaultRecord() throws Exception {
        int expectedValue = weatherDao.getDefaultRecords().size();
        weatherDao.createDefaultRecord(weather);
        assertEquals(expectedValue + 1, weatherDao.getDefaultRecords().size());
    }

    @Test(groups = "unit")
    public void testCreateGroupWeather() {
        int weatherCountBefore = weatherDao.getAllRecords().size();
        int weatherGroupBefore = weatherDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        weatherDao.createGroupRel(weather, researchGroup);
        weatherDao.create(weather);
        assertEquals(weatherCountBefore + 1, weatherDao.getAllRecords().size());

        List<Weather> list = weatherDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(weatherGroupBefore + 1, list.size());
    }

}
