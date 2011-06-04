/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import java.sql.Blob;

/**
 *
 * @author Jan Koren
 */
public class ScenarioTypeNonXml extends ScenarioType<Blob> implements java.io.Serializable {

  private Scenario scenario;
  private int scenarioId;
  private Blob scenarioXml;

  public ScenarioTypeNonXml() {
  }

  public int getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(int scenarioId) {
    this.scenarioId = scenarioId;
  }

  public Blob getScenarioXml() {
    return scenarioXml;
  }

  public void setScenarioXml(Blob scenarioXml) {
    this.scenarioXml = scenarioXml;
  }

  public Scenario getScenario() {
      return scenario;
  }

  public void setScenario(Scenario scenario) {
      this.scenario = scenario;
  }

}
