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
