package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * User: Tomas Pokryvka
 * Date: 23.4.13
 */
public class DigitizationDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected DigitizationDao digitizationDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected Digitization digitization;

  @Before
  public void setUp() throws Exception {
    digitization = new Digitization();
    digitization.setFilter("NotKnown");
    digitization.setGain(-1);
    digitization.setSamplingRate(-1);
  }

  @Test
  @Transactional
  public void createDigitalization(){
    int count = digitizationDao.getCountRecords();
    digitizationDao.create(digitization);
    assertEquals(count + 1, digitizationDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testGetDigitizationByParams() throws Exception {
    Digitization actualValue = digitizationDao.getDigitizationByParams(digitization.getSamplingRate(), digitization.getGain(), digitization.getFilter());
    assertTrue(digitization.getSamplingRate() - actualValue.getSamplingRate() < 0.000001);
    assertTrue(digitization.getGain() - actualValue.getGain() < 0.000001);
    assertEquals(digitization.getFilter(), actualValue.getFilter());
  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    ResearchGroup researchGroup  = researchGroupDao.getAllRecords().get(0);
    assertNotNull(researchGroup);
    assertEquals(0, digitization.getResearchGroups().size());
    digitizationDao.createGroupRel(digitization, researchGroup);
    assertEquals(1,digitization.getResearchGroups().size());
  }
}
