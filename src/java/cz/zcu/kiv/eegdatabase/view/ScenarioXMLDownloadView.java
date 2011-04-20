package cz.zcu.kiv.eegdatabase.view;

import java.sql.Blob;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;

/**
 * This view serves for downloading the data files.
 *
 * @author Jindra
 */
public class ScenarioXMLDownloadView extends AbstractView {
    Log log = LogFactory.getLog(getClass());

    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing view for scenario XML output");

        Scenario scenario = (Scenario) map.get("dataObject");
        Blob c = scenario.getScenarioXml();
        
        log.debug("Loading Scenario object - ID " + scenario.getScenarioId());

        log.debug("Setting MIME to XML");
        response.setHeader("Content-Type", "text/xml");

        log.debug("Setting filename");
        response.setHeader("Content-Disposition", "attachment;filename=scenario-" + scenario.getScenarioId());

        log.debug("Printing file content to output");
        response.getOutputStream().write(c.getBytes(1, (int) c.length()));

        log.debug("End of the view");
    }

}
