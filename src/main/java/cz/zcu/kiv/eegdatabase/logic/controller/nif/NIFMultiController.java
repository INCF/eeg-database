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
 *   NIFMultiController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.nif;

import cz.zcu.kiv.eegdatabase.logic.csv.CSVFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;


/**
 * Generating csv file from experiments or scenarios
 * User: pbruha
 * Date: 13.12.11
 * Time: 7:30
 * To change this template use File | Settings | File Templates.
 */
public class NIFMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private CSVFactory csvFactory;

    /**
     * Generates a csv file from scenarios
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView getScenariosCSVFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
                   log.debug("Controller for creating csv file for NIF");
        OutputStream out = null;
        int headerBufferSize = 8096;

        response.setHeader("Content-Type", "text/csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=EEGbase_scenarios.csv");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);

        log.debug("Generating csv");
        out = csvFactory.generateScenariosCsvFile();
        ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
        response.getOutputStream().write(bout.toByteArray());
        return null;
     }

       /**
     * Generates a csv file from experiments
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView getExperimentsCSVFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for csv file with creating experiments for NIF");
        OutputStream out = null;
        int headerBufferSize = 8096;

        response.setHeader("Content-Type", "text/csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=EEGbase_experiments.csv");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);

        log.debug("Generating csv");
        out = csvFactory.generateExperimentsCsvFile();
        ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
        response.getOutputStream().write(bout.toByteArray());
        return null;
     }
}
