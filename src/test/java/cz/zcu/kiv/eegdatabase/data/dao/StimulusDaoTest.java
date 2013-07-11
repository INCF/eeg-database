package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static junit.framework.Assert.*;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class StimulusDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected StimulusDao stimulusDao;
  protected Stimulus stimulus;


  @Before
  public void setUp() throws Exception {
    stimulus = new Stimulus();
    stimulus.setDescription("test-description");
  }

  @Test
  @Transactional
  public void testCreateStimulus() throws Exception {
    int count = stimulusDao.getCountRecords();
    System.out.println(count);
    stimulusDao.create(stimulus);
    assertEquals(count + 1, stimulusDao.getAllRecords().size());
  }

  @Test
  @Transactional
  public void testCanSaveDescription() throws Exception {
    assertFalse(stimulusDao.canSaveDescription("test-description"));
    assertTrue(stimulusDao.canSaveDescription(String.valueOf(new Random().nextLong())));
  }
}
