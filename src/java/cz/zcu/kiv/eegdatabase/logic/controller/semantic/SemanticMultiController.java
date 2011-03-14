package cz.zcu.kiv.eegdatabase.logic.controller.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

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
     * Transforms POJO object to resources of semantic web
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView generateOWL(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        String typeTransform = "";
        OutputStream out = null;
        byte[] bytes = null;

        typeTransform = request.getParameter("type");
        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=" + "eegdatabase.owl");

        log.debug("Creating output stream");
        out = response.getOutputStream();
        log.debug("Generating OWL");
        bytes = semanticFactory.transformPOJOToSemanticResource(typeTransform).toByteArray();
        response.setContentLength(bytes.length);
        out.write(bytes);
        out.flush();
        out.close();
        return null;
    }

    /**
     * Transforms POJO object to RDF
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView generateRDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        OutputStream out = null;
        byte[] bytes = null;

        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=" + "eegdatabase.rdf");
        log.debug("Creating output stream");
        out = response.getOutputStream();
        log.debug("Generating RDF");
        bytes = semanticFactory.generateRDF().toByteArray();
        response.setContentLength(bytes.length);
        out.write(bytes);
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