package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/scenarios")
public class ScenarioServiceController {

    @Autowired
    ScenarioService scenarioService;

    @RequestMapping("/all")
    public ScenarioDataList getAllScenarios(){
       return new ScenarioDataList(scenarioService.getAllScenarios());
    }

    @RequestMapping("/mine")
    public ScenarioDataList getMyScenarios(){
        return new ScenarioDataList(scenarioService.getMyScenarios());
    }


}
