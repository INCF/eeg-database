package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LOPP_6_WorWitOpp_L/. Contains document Testovaci scenare.docx.
 */
public class PersonOptParamDefPerformanceTest extends PerformanceTest {



    @Autowired
    PersonOptParamDefDao personOptParamDefDao;

    private PersonOptParamDef personOptParamDef;

/**
 * Method test create Hearing Impairment.
 * Identificator of test / PPT_LOPP_7_AddOpp_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createPersonOptParamTest(){
        personOptParamDef = new PersonOptParamDef();
        personOptParamDef.setParamName("personName");
        personOptParamDef.setParamDataType("personDataTyoe");
        personOptParamDefDao.create(personOptParamDef);
    }

/**
 * Method test edit Hearing Impairment.
 * Identificator of test / PPT_LOPP_8_EdiOpp_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editPersonOptParamTest(){
        personOptParamDef.setParamName("personName");
        personOptParamDef.setParamDataType("personDataTyoe");
        personOptParamDefDao.update(personOptParamDef);
    }
/**
 * Method test delete Hearing Impairment.
 * Identificator of test / PPT_LOPP_9_DelOpp_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deletePersonOptParamTest(){
        personOptParamDefDao.delete(personOptParamDef);
    }
}
