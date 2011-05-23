package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LHI_26_WorHi_L/. Contains document Testovaci scenare.docx.
 */
public class HearingImpairmentServicePerformanceTest extends PerformanceTest {

    @Autowired
    PersonDao personeDao;
    @Autowired
    HearingImpairmentDao hearingImpairmentDao;



    private HearingImpairment hearingImpairment;

/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LHI_27_AddHi_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createHearingImpairmentTest(){
        hearingImpairment = new HearingImpairment();
        hearingImpairment.setDescription("popisTest");
        hearingImpairment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        hearingImpairmentDao.create(hearingImpairment);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LHI_28_EditHi_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editHearingImpairmentTest(){
        hearingImpairment.setDescription("popisTest");
        hearingImpairment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        hearingImpairmentDao.update(hearingImpairment);
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LHI_29_DelWi_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteHearingImpairmentTest(){
        hearingImpairmentDao.delete(hearingImpairment);
    }

}
