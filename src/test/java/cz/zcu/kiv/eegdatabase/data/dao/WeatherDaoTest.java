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
 *   WeatherDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class WeatherDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected WeatherDao weatherDao;

  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected Weather weather;

  @Before
  public void init(){
    weather = new Weather();
    weather.setTitle("Test-title");
    weather.setDescription("Test-description");
    weather.setDefaultNumber(0);
  }

  @Test
  @Transactional
  public void testCreateWeather() throws Exception {
    int weatherCountBefore = weatherDao.getCountRecords();
    int weatherID = weatherDao.create(weather);
    assertEquals(weatherCountBefore + 1, weatherDao.getAllRecords().size());
    assertEquals(weatherID, weather.getWeatherId());
  }

 }
