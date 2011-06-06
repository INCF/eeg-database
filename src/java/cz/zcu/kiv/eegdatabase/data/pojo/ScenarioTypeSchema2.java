package cz.zcu.kiv.eegdatabase.data.pojo;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 31.5.11
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioTypeSchema2  extends ScenarioType<Document> implements java.io.Serializable {

    private Document scenarioXml;

    public ScenarioTypeSchema2() {
    }

    public Document getScenarioXml() {
      return scenarioXml;
    }

    public void setScenarioXml(Document scenarioXml) {
      this.scenarioXml = scenarioXml;
    }
}
