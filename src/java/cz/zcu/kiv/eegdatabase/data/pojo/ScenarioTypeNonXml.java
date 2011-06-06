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

  private Blob scenarioXml;

  public ScenarioTypeNonXml() {
  }

  public Blob getScenarioXml() {
    return scenarioXml;
  }

  public void setScenarioXml(Blob scenarioXml) {
    this.scenarioXml = scenarioXml;
  }
}
