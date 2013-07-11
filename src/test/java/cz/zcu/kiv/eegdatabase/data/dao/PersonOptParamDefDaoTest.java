package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class PersonOptParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected PersonOptParamDefDao personOptParamDefDao;

  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected PersonOptParamDef personOptParamDef;


  @Before
  public void setUp() throws Exception {
    personOptParamDef = new PersonOptParamDef();
    personOptParamDef.setParamDataType("test-paramDataType");
    personOptParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void testCreatePersonOptParamDef(){
    int count = personOptParamDefDao.getCountRecords();
    personOptParamDefDao.create(personOptParamDef);
    assertEquals(count + 1, personOptParamDefDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    int old = personOptParamDef.getResearchGroups().size();
    personOptParamDefDao.createGroupRel(personOptParamDef, researchGroup);
    assertEquals(old + 1, personOptParamDef.getResearchGroups().size());
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(personOptParamDefDao.isDefault(personOptParamDef.getPersonOptParamDefId()));
    personOptParamDefDao.createDefaultRecord(personOptParamDef);
    assertTrue(personOptParamDefDao.isDefault(personOptParamDef.getPersonOptParamDefId()));
  }
}
