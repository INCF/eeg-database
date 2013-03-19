package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleHardwareDaoTest extends AbstractDataAccessTest {


  @Autowired
  protected HardwareDao hardwareDao;

  protected Hardware hardware;

  @Before
  public void setUp() throws Exception {
    hardware = new Hardware();
    hardware.setTitle("New HW");
    hardware.setDescription("This is new testing HW");
    hardware.setType("HW type");
    hardware.setDefaultNumber(0);
  }



  @Test
  public void testCreateHardware() {
    int hardwareCountBefore  = hardwareDao.getAllRecords().size();
    int hardwareID = hardwareDao.create(hardware);
    assertEquals(hardwareCountBefore + 1, hardwareDao.getAllRecords().size());
    assertEquals(hardwareID, hardware.getHardwareId());
  }

  @Test
  public void testCanDelete() throws Exception {
    assertTrue(hardwareDao.canDelete(hardware.getHardwareId()));
  }

  @Test
  public void testCreateDefaultRecord() throws Exception {
    int expectedValue = hardwareDao.getDefaultRecords().size();
    hardwareDao.createDefaultRecord(hardware);
    assertEquals(expectedValue + 1, hardwareDao.getDefaultRecords().size());
  }

  @After
  public void tearDown(){
    hardwareDao.delete(hardware);
  }
}
