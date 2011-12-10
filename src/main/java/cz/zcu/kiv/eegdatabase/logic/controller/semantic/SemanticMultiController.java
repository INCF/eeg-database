package cz.zcu.kiv.eegdatabase.logic.controller.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import static org.apache.commons.io.IOUtils.copy;

/**
 * Controller for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 23.2.11
 * Time: 19:03
 */
public class SemanticMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
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
        String type = request.getParameter("type");

        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);
        out = response.getOutputStream();
        log.debug("Generating RDF");

        is = semanticFactory.generateOntology(type);

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
        String typeTransform;
        OutputStream out = null;
        InputStream is = null;
        int headerBufferSize = 8096;

        typeTransform = request.getParameter("type");
        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);
        out = response.getOutputStream();
        log.debug("Generating OWL");
        is = semanticFactory.generateOntologyOwlApi(typeTransform);

        copy(is, out);

        out.flush();
        out.close();
        return null;
    }

    /**
     * Sets semantic factory
     *
     * @param semanticFactory - semantic factory
     */
    public void setSemanticFactory(SemanticFactory semanticFactory) {
        this.semanticFactory = semanticFactory;
    }
}