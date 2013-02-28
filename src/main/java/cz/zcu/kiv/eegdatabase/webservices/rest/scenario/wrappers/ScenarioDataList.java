package cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@XmlRootElement(name = "scenarios")
public class ScenarioDataList {

    @XmlElement(name = "scenario")
    private List<ScenarioData> scenarios;

    public ScenarioDataList(){
        this.scenarios = Collections.emptyList();
    }

    public ScenarioDataList(List<ScenarioData> scenarios){
        this.scenarios = scenarios;
    }
}
