package cz.zcu.kiv.eegdatabase.data.pojo;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 31.5.11
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioTypeSchema3   extends ScenarioType<Document> {

    private Document scenarioXml;

    public ScenarioTypeSchema3() {
    }

    public Document getScenarioXml() {
      return scenarioXml;
    }

    public void setScenarioXml(Document scenarioXml) {
      this.scenarioXml = scenarioXml;
    }
}
