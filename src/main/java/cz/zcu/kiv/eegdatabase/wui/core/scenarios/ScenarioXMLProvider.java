package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.history.HistoryFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.type.ScenarioTypeFacade;

public class ScenarioXMLProvider {

    protected Log log = LogFactory.getLog(getClass());

    ScenariosFacade scenarioFacade;
    ScenarioTypeFacade scenarioTypeFacade;
    HistoryFacade historyFacade;
    PersonFacade personFacade;

    @Required
    public void setScenarioFacade(ScenariosFacade scenarioFacade) {
        this.scenarioFacade = scenarioFacade;
    }

    @Required
    public void setHistoryFacade(HistoryFacade historyFacade) {
        this.historyFacade = historyFacade;
    }

    @Required
    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }
    
    @Required
    public void setScenarioTypeFacade(ScenarioTypeFacade scenarioTypeFacade) {
        this.scenarioTypeFacade = scenarioTypeFacade;
    }
    
    @Transactional
    public DataFileDTO getXmlFileForScenario(int scenarioId, String loggedUserName) {

        try {
            DataFileDTO file = new DataFileDTO();
            Scenario scenario = scenarioFacade.read(scenarioId);
            ScenarioType scenarioXml = scenarioTypeFacade.read(scenarioId);

            byte[] fileOutput = toByteArray(scenarioXml.getScenarioXml());
            file.setFileContent(fileOutput);
            Person user = personFacade.getPerson(loggedUserName);
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            History history = new History();
            log.debug("Setting downloading scenario");
            history.setScenario(scenario);
            log.debug("Setting user");
            history.setPerson(user);
            log.debug("Setting time of download");
            history.setDateOfDownload(currentTimestamp);
            log.debug("Saving download history");
            historyFacade.create(history);
            return file;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private byte[] toByteArray(Object o) throws Exception {
        if (o instanceof Blob) {
            return ((Blob) o).getBytes(1, (int) ((Blob) o).length());
        }
        else if (o instanceof Document) {
            Source source = new DOMSource((Document) o);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);

            return out.toByteArray();
        }

        return null;
    }

}
