package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LWI_22_WorWitWi_L/. Contains document Testovaci scenare.docx.
 */
public class VisualImpairmentServicePerformanceTest extends PerformanceTest {

    @Autowired
    PersonDao personeDao;
    @Autowired
    VisualImpairmentDao visualImpairmentDao;
    private VisualImpairment visualImpairment;


/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LWI_23_AddWi_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createVisualImpairmentTest(){
        visualImpairment = new VisualImpairment();
        visualImpairment.setDescription("popisTest");
        visualImpairment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        visualImpairmentDao.create(visualImpairment);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LWI_24_EditWi_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editVisualImpairmentTest(){
        visualImpairment.setDescription("popisTest");
        visualImpairment.setPersons((Set<Person>) personeDao.getPerson("kaby"));
        visualImpairmentDao.update(visualImpairment);
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LWI_25_DelWi_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteVisualImpairmentTest(){
        visualImpairmentDao.delete(visualImpairment);
    }
}
