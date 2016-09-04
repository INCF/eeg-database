package cz.zcu.kiv.eegdatabase.wui.ui.semantic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.WebResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copyLarge;

/**
 * Created by petr-jezek on 26.8.16.
 */
public class SemanticUtil {

    private static Log log = LogFactory.getLog(SemanticUtil.class);

    public static void processSemanticStream(Page page, final InputStream stream) {
        page.getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

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
                    copyLarge(stream, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    log.error(e);
                }
            }


        });
    }
}
