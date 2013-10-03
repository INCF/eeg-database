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
 *   SemanticMultiController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copy;

/**
 * Controller for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 23.2.11
 * Time: 19:03
 */
public class SemanticMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private SemanticFactory semanticFactory;

    public SemanticMultiController() {

    }


    /**
     * Generates an ontology document from POJO objects.
     * This method gives the Jena's output.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView getOntology(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        OutputStream out = null;
        InputStream is = null;
        int headerBufferSize = 8096;
        String syntax = request.getParameter("type");

        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);
        out = response.getOutputStream();
        log.debug("Generating RDF");

        is = semanticFactory.getOntology(syntax);

        copy(is, out);
        out.flush();
        out.close();
        return null;
    }

    /**
     * Generates an ontology document from POJO objects.
     * This method transforms the Jena's output using Owl-Api.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView getOntologyOwlApi(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        String syntax;
        OutputStream out = null;
        InputStream is = null;
        int headerBufferSize = 8096;

        syntax = request.getParameter("type");
        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);
        out = response.getOutputStream();
        log.debug("Generating OWL");
        is = semanticFactory.getOntologyOwlApi(syntax);

        copy(is, out);

        out.flush();
        out.close();
        return null;
    }


    /**
     * Generates an ontology document from POJO objects.
     * This method gives the Jena's output.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView getOntologyStructure(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        OutputStream out = null;
        InputStream is = null;
        int headerBufferSize = 8096;
        String syntax = request.getParameter("type");

        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);
        out = response.getOutputStream();
        log.debug("Generating RDF");

        is = semanticFactory.getOntologySchema(syntax);

        copy(is, out);
        out.flush();
        out.close();
        return null;
    }

}
