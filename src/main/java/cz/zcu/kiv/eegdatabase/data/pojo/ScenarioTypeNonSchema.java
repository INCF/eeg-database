/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.pojo;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Jan Koren
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_NONSCHEMA")
@PrimaryKeyJoinColumn(name = "SCENARIO_ID", referencedColumnName = "SCENARIO_ID")
public class ScenarioTypeNonSchema extends ScenarioType<Document> {
  @Column(name = "SCENARIO_XML")
  @Type(type = "cz.zcu.kiv.eegdatabase.data.datatypes.OracleXMLType")
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
