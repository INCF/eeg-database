/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import java.sql.Blob;

/**
 *
 * @author Jan Koren
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_NONXML")
@PrimaryKeyJoinColumn(name = "SCENARIO_ID", referencedColumnName = "SCENARIO_ID")
public class ScenarioTypeNonXml extends ScenarioType<Blob> {
  @Lob
  @Column(name = "SCENARIO_XML")
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
