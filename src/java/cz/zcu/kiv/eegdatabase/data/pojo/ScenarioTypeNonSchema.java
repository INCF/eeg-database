/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import org.w3c.dom.Document;

/**
 *
 * @author Jan Koren
 */
public class ScenarioTypeNonSchema extends ScenarioType<Document> implements java.io.Serializable {

  private Document scenarioXml;

  public ScenarioTypeNonSchema() {
  }

  public Document getScenarioXml() {
    return scenarioXml;
  }

  public void setScenarioXml(Document scenarioXml) {
    this.scenarioXml = scenarioXml;
  }
}
