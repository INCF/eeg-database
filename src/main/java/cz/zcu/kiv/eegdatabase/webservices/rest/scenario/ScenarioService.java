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
 *   ScenarioService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Scenario REST service.
 *
 * @author Petr Miko
 */
public interface ScenarioService {

    /**
     * Getter of all scenarios on server.
     *
     * @return scenarios
     */
    public List<ScenarioData> getAllScenarios();

    /**
     * Getter of user's scenarios.
     *
     * @return scenarios
     */
    public List<ScenarioData> getMyScenarios();

    /**
     * Creates new scenario record.
     *
     * @param scenarioData scenario information
     * @param file         scenario file
     * @return scenario identifier
     * @throws IOException                  error while creating scenario file
     * @throws SAXException                 error while accessing scenario file XML content
     * @throws ParserConfigurationException error while accessing scenario file XML content
     */
    public int create(ScenarioData scenarioData, MultipartFile file) throws IOException, SAXException, ParserConfigurationException;

    /**
     * Method for writing file content into HTTP response.
     * Used for file download.
     *
     * @param id       scenario identifier
     * @param response HTTP response
     * @throws RestServiceException  error while accessing the file
     * @throws SQLException          error while reading file from DB
     * @throws IOException           error while reading the file
     * @throws TransformerException  error while transforming XML scenario file
     * @throws RestNotFoundException error, when no file with such id found on server
     */
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, TransformerException, RestNotFoundException;
}
