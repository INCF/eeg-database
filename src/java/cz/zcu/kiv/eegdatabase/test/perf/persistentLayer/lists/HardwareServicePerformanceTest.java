package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 * Identificator of test PPT_LHW_2_WorWitHw_L. Contains document Testovaci scenare.docx.
 */
public class HardwareServicePerformanceTest extends PerformanceTest {

        @Autowired
        PersonDao personeDao;
    @Autowired
    HearingImpairmentDao hearingImpairmentDao;
    @Autowired
    HardwareDao hardwareDao;


    private Hardware hardware;

/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LHW_3_AddHw_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createHardwareTest(){
        hardware = new Hardware();
        hardware.setDescription("popisTest");
        hardware.setTitle("titulekHardware");
        hardwareDao.create(hardware);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LHW_4_EdiWitHw_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editHardwareTest(){
       hardware.setDescription("popisTest");
        hardware.setTitle("titulekHardware");
        hardwareDao.update(hardware);
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LHW_4_DelWitHw_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteHardwareTest(){
        hardwareDao.delete(hardware);
    }
}
