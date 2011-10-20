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
     * Transforms POJO object to RDF
     *
     * @return
     * @throws WebServiceException
     * @throws IOException
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler generateRDF() throws SOAPException;

    /**
     * Transforms POJO object to resources of semantic web
     *
     * @param type  - syntax (rdf,owl,ttl)
     * @return
     * @throws WebServiceException
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler generateOWL(String type) throws SOAPException;
}
