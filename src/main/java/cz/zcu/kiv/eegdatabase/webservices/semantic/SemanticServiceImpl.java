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
 *   SemanticServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SimpleSemanticFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Webservice for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 17.3.11
 * Time: 18:39
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.semantic.SemanticService")
public class SemanticServiceImpl implements SemanticService {
    private SimpleSemanticFactory simpleSemanticFactory;
    private Log log = LogFactory.getLog(getClass());

    /**
     * Method just for checking web service availability
     *
     * @return
     */
    public boolean isServiceAvailable() {
        return true;
    }

    /**
     * Generates an ontology document from POJO objects.
     * This method gives the Jena's output.
     *
     * @return
     * @throws WebServiceException
     * @throws IOException
     */
    public DataHandler getOntology() throws SOAPException {
        InputStream is = null;
        byte[] bytes = null;
        ByteArrayDataSource rdf = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            int i;
            is = simpleSemanticFactory.getOntology(null);
            while ((i = is.read()) > -1) {
                os.write(i);
            }
        } catch (IOException e) {
            log.error(e);
            throw new SOAPException(e);
        }
        rdf = new ByteArrayDataSource(os.toByteArray(), "fileBinaryStream");
        return new DataHandler(rdf);
    }

    /**
     * Generates an ontology document from POJO objects.
     * This method transforms the Jena's output using Owl-Api.
     *
     * @param syntaxType - syntax (rdf,owl,ttl)
     * @return
     * @throws WebServiceException
     * @throws IOException
     * @throws OWLOntologyStorageException
     * @throws OWLOntologyCreationException
     */
    public DataHandler getOntologyOwlApi(String syntaxType) throws SOAPException {
        InputStream is = null;
        byte[] bytes = null;
        ByteArrayDataSource owl = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            int i;
            is = simpleSemanticFactory.getOntologyOwlApi(syntaxType);
            while ((i = is.read()) > -1) {
                os.write(i);
            }
        } catch (OWLOntologyCreationException e) {
            log.error(e);
            throw new SOAPException(e);
        } catch (OWLOntologyStorageException e) {
            log.error(e);
            throw new SOAPException(e);
        } catch (IOException e) {
            log.error(e);
            throw new SOAPException(e);
        }
        owl = new ByteArrayDataSource(os.toByteArray(), "fileBinaryStream");
        return new DataHandler(owl);
    }

    /**
     * Setting semantic factory
     * @param simpleSemanticFactory - semantic factory
     */
    public void setSimpleSemanticFactory(SimpleSemanticFactory simpleSemanticFactory) {
        this.simpleSemanticFactory = simpleSemanticFactory;
    }
}
