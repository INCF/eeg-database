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
 *   DataFileService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Data file REST service interface.
 * Defines required service capabilities.
 *
 * @author Petr Miko
 */
public interface DataFileService {

    /**
     * Creates new DataFile under specified experiment.
     *
     * @param experimentId experiment identifier
     * @param description  data file description
     * @param file         data file multipart
     * @return identifier of created record
     * @throws IOException error while creating record
     */
    public int create(int experimentId, String description, MultipartFile file) throws IOException;

    /**
     * Method for downloading file from server.
     *
     * @param id       data file identifier
     * @param response HTTP response
     * @throws RestServiceException  error while accessing to file
     * @throws SQLException          error while reading file from db
     * @throws IOException           error while reading file
     * @throws RestNotFoundException no such file on server
     */
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, RestNotFoundException;
}
