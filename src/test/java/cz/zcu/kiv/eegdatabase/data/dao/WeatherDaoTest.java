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