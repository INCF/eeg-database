package cz.zcu.kiv.eegdatabase.logic.controller.semantic;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * Controller for transforming POJO object to resources of semantic web
 * User: pbruha
 * Date: 23.2.11
 * Time: 19:03
 */
public class TransformerMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    private List<GenericDao> gDaoList = new ArrayList<GenericDao>();
    private OutputStream os = new ByteArrayOutputStream();
    private List dataList = new ArrayList();
    private SemanticFactory semanticFactory;

    public TransformerMultiController() {

    }

    /**
     * Transforms POJO object to resources of semantic web
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView generateOWL(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        String typeTransform = "";
        typeTransform = request.getParameter("type");
        response.setHeader("Content-Type", "application/rdf xml");
        response.setHeader("Content-Disposition", "attachment;filename=" + "eegdatabase");
        log.debug("Creating output stream");
        response.getOutputStream().write(semanticFactory.transformPOJOToSemanticResource(typeTransform).toByteArray());
        return null;
    }

    /**
     * Transforms POJO object to RDF
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView generateRDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for transforming POJO object to resources of semantic web");
        response.setHeader("Content-Type", "application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=" + "eegdatabase");
        log.debug("Creating output stream");
        response.getOutputStream().write(semanticFactory.generateRDF().toByteArray());
        return null;
    }

    /**
     * Sets semantic factory
     * @param semanticFactory - semantic factory
     */
    public void setSemanticFactory(SemanticFactory semanticFactory) {
        this.semanticFactory = semanticFactory;
    }
}