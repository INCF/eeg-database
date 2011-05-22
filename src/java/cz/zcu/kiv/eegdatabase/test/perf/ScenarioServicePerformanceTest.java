package cz.zcu.kiv.eegdatabase.test.perf;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioServicePerformanceTest extends PerformanceTest{

    @Autowired
    ScenarioDao scenarioDao;

    private Scenario scenario;

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
