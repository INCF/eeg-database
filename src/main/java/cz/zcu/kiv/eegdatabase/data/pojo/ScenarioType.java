package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 31.5.11
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class ScenarioType<T> implements IScenarioType<T>, java.io.Serializable {

    private int scenarioId;
    private Scenario scenario;

    public int getScenarioId() {
      return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
      this.scenarioId = scenarioId;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public abstract T getScenarioXml();

    public abstract void setScenarioXml(T scenarioXml);
}
