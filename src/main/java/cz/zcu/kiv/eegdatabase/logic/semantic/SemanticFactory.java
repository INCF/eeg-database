package cz.zcu.kiv.eegdatabase.logic.semantic;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.context.ApplicationContext;

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
     * This method returns a serialization of the Jena's ontology model
     * of POJO objects in a specified syntax.
     *
     * @param syntax - required syntax of the ontology document
     * @return is - ontology document
     * @throws java.io.IOException - if an I/O error occurs.
     */
    public InputStream generateOntology(String syntax) throws IOException;

    /**
     * Generates an ontology document from POJO objects.<br>
     * This method returns a serialization of the Jena's ontology model
     * of POJO objects in a specified syntax. If the <code>structureOnly</code> argument
     * is set true then the output ontology document describes only static structure
     * of data, but not the data itself.
     *
     * @param syntax - required syntax of the ontology document
     * @param structureOnly - if true generated ontology contains only static structure of data
     * @return is - ontology document
     * @throws java.io.IOException - if an I/O error occurs.
     */
    public InputStream generateOntology(String syntax, boolean structureOnly) throws IOException;


    /**
     * Generates an ontology document from POJO objects.<br>
     * This method gets a serialization of the Jena's ontology model in a default
     * syntax and transforms it using Owl-Api. The Owl-Api's output syntax
     * is specified by the syntax parameter.
     *
     * @param syntax - param from user (rdf, owl, ttl)
     * @return is - generated ontology document (rdf, owl, ttl)
     * @throws java.io.IOException - if an I/O error occurs.
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException
     *                             - if an error occurs in Owl-Api while loading the ontology.
     * @throws org.semanticweb.owlapi.model.OWLOntologyStorageException
     *                             - if an error occurs in Owl-Api while creating the output.
     */
    public InputStream generateOntologyOwlApi(String syntax) throws IOException,
            OWLOntologyCreationException, OWLOntologyStorageException;
}
