/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import org.w3c.dom.Document;

/**
 * Mapping ScenarioType tables - ScenarioType objects
 * @author Jan Koren
 */
public class ScenarioType1 implements java.io.Serializable {

  private int scenarioId;
  private Document scenarioXml;

  public ScenarioType1() {
  }

  public ScenarioType1(int scenarioId, Document scenarioXml) {
    this.scenarioId = scenarioId;
    this.scenarioXml = scenarioXml;
  }

  public int getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(int scenarioId) {
    this.scenarioId = scenarioId;
  }

  public Document getScenarioXml() {
    return scenarioXml;
  }

  public void setScenarioXml(Document scenarioXml) {
    this.scenarioXml = scenarioXml;
  }
  
}
