package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@XmlType(propOrder = {"scenarioId", "scenarioName"})
@XmlRootElement(name = "scenario")
public class ScenarioSimpleData {

    private int scenarioId;
    private String scenarioName;


    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

}
