package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 31.5.11
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class ScenarioType<T> {

    private int scenarioId;

    public int getScenarioId() {
      return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
      this.scenarioId = scenarioId;
    }

    public abstract T getScenarioXml();

    public abstract void setScenarioXml(T scenarioXml);

    public abstract Scenario getScenario();

    public abstract void setScenario(Scenario scenario);
}
