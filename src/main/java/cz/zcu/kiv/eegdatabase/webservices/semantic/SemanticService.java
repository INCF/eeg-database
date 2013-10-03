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
 *   SemanticService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.semantic;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;
import java.io.IOException;

/**
 * Interface for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 17.3.11
 * Time: 18:43
 */
@MTOM
@WebService
public interface SemanticService {

    /**
     * Method just for checking web service availability
     *
     * @return
     */
    public boolean isServiceAvailable();

    /**
     * Generates an ontology document from POJO objects.
     * This method gives the Jena's output.
     *
     * @return
     * @throws WebServiceException
     * @throws IOException
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler getOntology() throws SOAPException;

    /**
     * Generates an ontology document from POJO objects.
     * This method transforms the Jena's output using Owl-Api.
     *
     * @param type  - syntax (rdf,owl,ttl)
     * @return
     * @throws WebServiceException
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler getOntologyOwlApi(String type) throws SOAPException;
}
