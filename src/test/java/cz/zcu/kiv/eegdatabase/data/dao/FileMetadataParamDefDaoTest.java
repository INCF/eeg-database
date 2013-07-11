package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
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
public class FileMetadataParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected FileMetadataParamDefDao fileMetadataParamDefDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected FileMetadataParamDef fileMetadataParamDef;

  @Before
  public void setUp() throws Exception {
    fileMetadataParamDef = new FileMetadataParamDef();
    fileMetadataParamDef.setParamDataType("test-paramDataType");
    fileMetadataParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void testCreateFileMetadataParamDef(){
    int count = fileMetadataParamDefDao.getCountRecords();
    fileMetadataParamDefDao.create(fileMetadataParamDef);
    assertEquals(count + 1, fileMetadataParamDefDao.getCountRecords()) ;
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(fileMetadataParamDefDao.isDefault(fileMetadataParamDef.getFileMetadataParamDefId()));
    fileMetadataParamDefDao.createDefaultRecord(fileMetadataParamDef);
    assertTrue(fileMetadataParamDefDao.isDefault(fileMetadataParamDef.getFileMetadataParamDefId()));
  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    int old = fileMetadataParamDef.getResearchGroups().size();
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    fileMetadataParamDefDao.createGroupRel(fileMetadataParamDef, researchGroup);
    assertEquals(old + 1, fileMetadataParamDef.getResearchGroups().size());
  }
}
