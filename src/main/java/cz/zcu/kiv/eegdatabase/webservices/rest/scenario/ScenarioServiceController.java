package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioDataList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/scenarios")
public class ScenarioServiceController {

    private final static Log log = LogFactory.getLog(ScenarioServiceController.class);

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao researchGroupDao;

    @Autowired
    private ScenarioService scenarioService;

    @RequestMapping("/all")
    public ScenarioDataList getAllScenarios(){
       return new ScenarioDataList(scenarioService.getAllScenarios());
    }

    @RequestMapping("/mine")
    public ScenarioDataList getMyScenarios(){
        return new ScenarioDataList(scenarioService.getMyScenarios());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ScenarioData create(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("scenarioName") String scenarioName, @RequestParam("researchGroupId") int researchGroupId,
                       @RequestParam(value = "mimeType", required = false) String mimeType,
                       @RequestParam(value = "private", required = false) boolean isPrivate,
                       @RequestParam("description") String description,
                       @RequestParam("file") MultipartFile file) throws RestServiceException {
        ScenarioData scenarioData = new ScenarioData();
        scenarioData.setScenarioName(scenarioName);
        scenarioData.setResearchGroupId(researchGroupId);
        scenarioData.setDescription(description);
        scenarioData.setMimeType(mimeType != null ? mimeType : file.getContentType());
        scenarioData.setPrivate(isPrivate);

        try {
            int pk = scenarioService.create(scenarioData, file);
            scenarioData.setScenarioId(pk);

            Person user = personDao.getLoggedPerson();
            scenarioData.setOwnerName(user.getGivenname() + " " + user.getSurname());
            ResearchGroup group = researchGroupDao.read(scenarioData.getResearchGroupId());
            scenarioData.setResearchGroupName(group.getTitle());

            response.addHeader("Location", buildLocation(request, pk));
            return scenarioData;
        } catch (IOException e) {
            log.error(e);
            throw new RestServiceException(e);
        } catch (SAXException e) {
            log.error(e);
            throw new RestServiceException(e);
        } catch (ParserConfigurationException e) {
            log.error(e);
            throw new RestServiceException(e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") int id, HttpServletResponse response) throws RestServiceException, RestNotFoundException {
        try {
            scenarioService.getFile(id, response);
        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(ex);
        } catch (SQLException e) {
            log.error("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(e);
        } catch (TransformerException e) {
            log.error("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(e);
        }
    }

    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{id}").toString());
        return ut.expand(id).toASCIIString();
    }

    @ExceptionHandler(RestServiceException.class)
    public void handleException(RestServiceException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        log.error(ex);
    }

    @ExceptionHandler(RestNotFoundException.class)
    public void handleException(RestNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        log.error(ex);
    }


}
