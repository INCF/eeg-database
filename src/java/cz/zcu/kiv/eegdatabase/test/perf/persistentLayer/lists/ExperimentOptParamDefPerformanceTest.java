package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.PerformanceTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LOPP_6_WorWitOpp_L/. Contains document Testovaci scenare.docx.
 */
public class ExperimentOptParamDefPerformanceTest extends PerformanceTest {

    @Autowired
    PersonDao personeDao;

    @Autowired
    ExperimentOptParamDefDao experimentOptParamDefDao;


    private HearingImpairment hearingImpairment;
    private ExperimentOptParamDef experimentOptParamDef;

/**
 * Method test create Experiment Opt.
 * Identificator of test /PPT_LOPP_7_AddOpp_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void createExperimentOptParamDef(){
        experimentOptParamDef = new ExperimentOptParamDef();
        experimentOptParamDef.setParamName("nameTest");
        experimentOptParamDef.setParamDataType("type");
        experimentOptParamDefDao.create(experimentOptParamDef);
    }

/**
 * Method test edit Experiment Opt.
 * Identificator of test / PPT_LOPP_8_EditOpp_F /. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void editExperimentOptParamDef(){
        experimentOptParamDef.setParamName("nameTest");
        experimentOptParamDef.setParamDataType("type");
        experimentOptParamDefDao.update(experimentOptParamDef);
    }
/**
 * Method test delete Experiment Opt.
 * Identificator of test /PPT_LOPP_9_DelOpp_F/. Contains document Testovaci scenare.docx.
 */
    //@Test
    //@PerfTest(invocations = 2, threads = 2)
    public void deleteExperimentOptParamDef(){
        experimentOptParamDefDao.delete(experimentOptParamDef);
    }

}
