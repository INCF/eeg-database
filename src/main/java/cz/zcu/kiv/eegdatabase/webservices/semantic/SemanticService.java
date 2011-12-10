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
