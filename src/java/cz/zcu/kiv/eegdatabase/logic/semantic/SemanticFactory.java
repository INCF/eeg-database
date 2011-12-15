package cz.zcu.kiv.eegdatabase.logic.semantic;

import cz.zcu.kiv.jenaBean.JenaBeanTool;
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
     * Transforms POJO object to resouces of semantic web
     * @param typeTransform - param from user (rdf, owl, ttl)
     * @return  bout - generated resource of semantic web(rdf, owl, ttl)
     * @throws IOException
     * @throws OWLOntologyStorageException
     * @throws OWLOntologyCreationException
     */
    public InputStream transformPOJOToSemanticResource(String typeTransform) throws IOException, OWLOntologyStorageException, OWLOntologyCreationException;

    /**
     * Generates RDF
     * @return  is - RDF
     * @throws IOException
     */
     public InputStream  generateRDF() throws IOException;

}
