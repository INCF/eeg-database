package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public interface ScenarioService {

    public List<ScenarioData> getAllScenarios();

    public List<ScenarioData> getMyScenarios();

    public int create(ScenarioData scenarioData, MultipartFile file) throws IOException, SAXException, ParserConfigurationException;

    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, TransformerException, RestNotFoundException;
}
