package cz.zcu.kiv.eegdatabase.webservices.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.WebServiceException;
import java.io.IOException;

/**
 * Webservice for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 17.3.11
 * Time: 18:39
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.semantic.SemanticService")
public class SemanticServiceImpl implements SemanticService {
    private SemanticFactory semanticFactory;

    /**
     * Method just for checking web service availability
     *
     * @return
     */
    public boolean isServiceAvailable() {
        return true;
    }

    /**
     * Transforms POJO object to RDF
     *
     * @return
     * @throws WebServiceException
     * @throws IOException
     */
    public DataHandler generateRDF() throws WebServiceException {
        byte[] bytes = null;
        try {
            bytes = semanticFactory.generateRDF().toByteArray();
        } catch (IOException e) {
            throw new WebServiceException(e.getMessage(), e.getCause());
        }

        ByteArrayDataSource rdf = null;
        rdf = new ByteArrayDataSource(bytes, "fileBinaryStream");
        return new DataHandler(rdf);
    }

    /**
     * Transforms POJO object to resources of semantic web
     *
     * @param syntaxType - syntax (rdf,owl,ttl)
     * @return
     * @throws WebServiceException
     * @throws IOException
     * @throws OWLOntologyStorageException
     * @throws OWLOntologyCreationException
     */
    public DataHandler generateOWL(String syntaxType) throws WebServiceException {
        byte[] bytes = null;
        try {
            bytes = semanticFactory.transformPOJOToSemanticResource(syntaxType).toByteArray();
        } catch (IOException e) {
            throw new WebServiceException(e.getMessage(), e.getCause());
        } catch (OWLOntologyStorageException e) {
            throw new WebServiceException(e.getMessage(), e.getCause());
        } catch (OWLOntologyCreationException e) {
            throw new WebServiceException(e.getMessage(), e.getCause());
        }

        ByteArrayDataSource owl = null;
        owl = new ByteArrayDataSource(bytes, "fileBinaryStream");
        return new DataHandler(owl);
    }

    public void setSemanticFactory(SemanticFactory semanticFactory) {
        this.semanticFactory = semanticFactory;
    }
}
