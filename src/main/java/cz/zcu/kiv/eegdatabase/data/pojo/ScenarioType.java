package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 31.5.11
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_PARENT")
public abstract class ScenarioType<T> implements IScenarioType<T>, java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SCENARIO_ID")
    private int scenarioId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="SCENARIO_ID")
    @PrimaryKeyJoinColumn
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
