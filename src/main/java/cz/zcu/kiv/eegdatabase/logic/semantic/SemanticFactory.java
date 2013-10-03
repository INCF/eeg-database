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
 *   SemanticFactory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.semantic;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 14.12.11
 * Time: 9:28
 * To change this template use File | Settings | File Templates.
 */
public interface SemanticFactory {

    /**
     * Generates an ontology document from POJO objects.<br>
     * This method returns the serialization from Jena in a specified syntax.
     *
     * @param syntax - required syntax of the ontology document
     * @return is - ontology document
     * @throws java.io.IOException - if an I/O error occurs.
     */
    public InputStream getOntology(String syntax) throws IOException;


    /**
     * Generates an ontology schema document from POJO objects.<br>
     * This method returns the serialization from Jena in a specified syntax.
     * The output document describes schema of the ontology (i.e. defined
     * classes and properties), but contains no data itself.
     *
     * @param syntax - required syntax of the ontology schema document
     * @return is - ontology schema document
     * @throws java.io.IOException - if an I/O error occurs.
     */
    public InputStream getOntologySchema(String syntax) throws IOException;


    /**
     * Generates an ontology document from POJO objects.<br>
     * This method transforms the serialization from Jena using the Owl-Api.
     * The Owl-Api's output syntax is specified by the <code>syntax</code> parameter.
     *
     * @param syntax - param from user (owl/xml, rdf/xml, turtle, owl-functional)
     * @return is - generated ontology document
     * @throws java.io.IOException - if an I/O error occurs.
     * @throws OWLOntologyCreationException
     *                             - if an error occurs in Owl-Api while loading the ontology.
     * @throws OWLOntologyStorageException
     *                             - if an error occurs in Owl-Api while creating the output.
     */
    public InputStream getOntologyOwlApi(String syntax) throws IOException,
            OWLOntologyCreationException, OWLOntologyStorageException;


}
