package cz.zcu.kiv.eegdatabase.wui.ui.semantic;

import cz.zcu.kiv.eegdatabase.logic.semantic.SemanticFactory;
import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copy;

import static org.apache.commons.io.IOUtils.copy;
import static org.apache.commons.io.IOUtils.copyLarge;

/**
 * Created by petr-jezek on 25.8.16.
 */
public class SemanticPage extends Page {

    private Log log = LogFactory.getLog(getClass());
    @SpringBean
    private SemanticFactory semanticFactory;


    public SemanticPage(final PageParameters parameters) throws Exception {


        log.debug("Controller for transforming POJO object to resources of semantic web");
        StringValue type = parameters.get("type");
        log.debug("type: " + type);
        final InputStream is = semanticFactory.getOntology(type.toString());


        getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            public void detach(IRequestCycle requestCycle) {
                // Nothing to do here.
            }

            public void respond(IRequestCycle requestCycle) {
                log.debug("Creating output stream");
                WebResponse response = (WebResponse) requestCycle.getResponse();
                response.setContentType("application/rdf+xml");
                response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");
                response.setStatus(HttpServletResponse.SC_OK);
                OutputStream out = response.getOutputStream();
                try {
                    log.debug("Generating RDF");
                    copyLarge(is, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                   log.error(e);
                }
            }


        });


        /*
        String syntax = request.getParameter("type");

        response.setHeader("Content-Type", "application/rdf+xml");
        response.setContentType("application/rdf+xml");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.owl");

        log.debug("Creating output stream");

        out = response.getOutputStream();
        log.debug("Generating RDF");
*/

    }
}
