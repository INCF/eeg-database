package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class ExperimentOptParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected ExperimentOptParamDefDao experimentOptParamDefDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;


  protected ExperimentOptParamDef experimentOptParamDef;

  @Before
  public void setUp() throws Exception {
    experimentOptParamDef = new ExperimentOptParamDef();
    experimentOptParamDef.setParamDataType("test-paramDataType");
    experimentOptParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void createExperimentOptParamDef(){
    int count = experimentOptParamDefDao.getCountRecords();
    experimentOptParamDefDao.create(experimentOptParamDef);
    assertEquals(count + 1, experimentOptParamDefDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(experimentOptParamDefDao.isDefault(experimentOptParamDef.getExperimentOptParamDefId()));
    experimentOptParamDefDao.createDefaultRecord(experimentOptParamDef);
    assertTrue(experimentOptParamDefDao.isDefault(experimentOptParamDef.getExperimentOptParamDefId()));
  }

  @Test
  public void testGetDefaultRecords() throws Exception {

  }


  @Test
  public void testDeleteGroupRel() throws Exception {

  }

  @Test
  public void testGetGroupRel() throws Exception {

  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    int old = experimentOptParamDef.getResearchGroups().size();
    experimentOptParamDefDao.createGroupRel(experimentOptParamDef, researchGroup);
    assertEquals(old + 1, experimentOptParamDef.getResearchGroups().size());
  }
}
