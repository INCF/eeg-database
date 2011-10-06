package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 6.6.11
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public interface IScenarioType<T> {
    public int getScenarioId();
    public void setScenarioId(int scenarioId);
    public Scenario getScenario();
    public void setScenario(Scenario scenario);
    public T getScenarioXml();
    public void setScenarioXml(T scenarioXml);
}
