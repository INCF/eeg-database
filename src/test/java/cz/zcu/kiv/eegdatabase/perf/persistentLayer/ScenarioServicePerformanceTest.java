package cz.zcu.kiv.eegdatabase.perf.persistentLayer;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 * Identificator of test /PPT_S_1_WorWitSce_L/. Contains document Testovaci scenare.docx.
 */
public class ScenarioServicePerformanceTest extends PerformanceTest{

    @Autowired
    ScenarioDao scenarioDao;

    private Scenario scenario;

    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_S_2_AddSce_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void createScenarioTest(){
      scenario = new Scenario();
        scenario.setDescription("testovaci scenar");
//        scenario.setExperiments();
//        scenario.setMimetype();
//        scenario.setPerson();
//        scenario.setResearchGroup();
      scenarioDao.create(scenario);
    }
    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_S_3_EdiSce_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void editScenarioTest(){
      scenario.setDescription("testovaci scenar");
//        scenario.setExperiments();
//        scenario.setMimetype();
//        scenario.setPerson();
//        scenario.setResearchGroup();
      scenarioDao.update(scenario);

    }
}
