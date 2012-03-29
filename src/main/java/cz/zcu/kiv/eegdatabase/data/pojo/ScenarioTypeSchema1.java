package cz.zcu.kiv.eegdatabase.data.pojo;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 31.5.11
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_SCHEMA1")
@PrimaryKeyJoinColumn(name = "SCENARIO_ID", referencedColumnName = "SCENARIO_ID")
public class ScenarioTypeSchema1  extends ScenarioType<Document> {
    @Column(name = "SCENARIO_XML")
    @Type(type = "cz.zcu.kiv.eegdatabase.data.datatypes.OracleXMLType")
    private Document scenarioXml;

    public ScenarioTypeSchema1() {
    }

    public Document getScenarioXml() {
      return scenarioXml;
    }

    public void setScenarioXml(Document scenarioXml) {
      this.scenarioXml = scenarioXml;
    }
}
