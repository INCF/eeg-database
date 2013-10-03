/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ScenarioServiceController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
import org.springframework.web.util.UriTemplate;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for mapping requests upon scenario REST service.
 *
 * @author Petr Miko
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

    /**
     * Getter of all scenarios on server.
     *
     * @return scenarios
     */
    @RequestMapping("/all")
    public ScenarioDataList getAllScenarios() {
        return new ScenarioDataList(scenarioService.getAllScenarios());
    }

    /**
     * Getter of user's scenarios.
     *
     * @return scenarios
     */
    @RequestMapping("/mine")
    public ScenarioDataList getMyScenarios() {
        return new ScenarioDataList(scenarioService.getMyScenarios());
    }

    /**
     * Creates new scenario record.
     *
     * @param request         HTTP request
     * @param response        HTTP response
     * @param scenarioName    scenario name
     * @param researchGroupId research group to which should be new record assigned
     * @param mimeType        scenario file MIME type
     * @param isPrivate       is scenario private
     * @param description     scenario description
     * @param file            file content
     * @return filled scenario data container
     * @throws RestServiceException error while creating new scenario record
     */
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
        scenarioData.setFileName(file.getName());
        scenarioData.setFileLength((int) file.getSize());

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

    /**
     * Method for writing file content into HTTP response.
     * Used for file download.
     *
     * @param id       scenario identifier
     * @param response HTTP response
     * @throws RestServiceException  error while accessing the file
     * @throws RestNotFoundException error, when no file with such id found on server
     */
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

    /**
     * Method for creating download URL to scenario file.
     *
     * @param request HTTP request
     * @param id      scenario identifier
     * @return scenario file download location
     */
    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{id}").toString());
        return ut.expand(id).toASCIIString();
    }

    /**
     * Exception handler for RestServiceException.class.
     * Writes exception message into HTTP response.
     *
     * @param ex       exception body
     * @param response HTTP response
     * @throws IOException error while writing into response
     */
    @ExceptionHandler(RestServiceException.class)
    public void handleException(RestServiceException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        log.error(ex);
    }

    /**
     * Exception handler for RestNotFoundException.class.
     * Writes exception message into HTTP response.
     *
     * @param ex       exception body
     * @param response HTTP response
     * @throws IOException error while writing into response
     */
    @ExceptionHandler(RestNotFoundException.class)
    public void handleException(RestNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        log.error(ex);
    }

}
