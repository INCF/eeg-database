package cz.zcu.kiv.eegdatabase.logic.controller.nif;

import cz.zcu.kiv.eegdatabase.logic.csv.CSVFactory;
import cz.zcu.kiv.eegdatabase.logic.csv.SimpleCSVFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;


/**
 * Generating csv file from experiments or scenarios
 * User: pbruha
 * Date: 13.12.11
 * Time: 7:30
 * To change this template use File | Settings | File Templates.
 */
public class NIFMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private CSVFactory csvFactory;

    /**
     * Generates a csv file from scenarios
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView getScenariosCSVFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
                   log.debug("Controller for creating csv file for NIF");
        OutputStream out = null;
        int headerBufferSize = 8096;

        response.setHeader("Content-Type", "text/csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=EEGbase_scenarios_example_2011.csv");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);

        log.debug("Generating csv");
        out = csvFactory.generateScenariosCsvFile();
        ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
        response.getOutputStream().write(bout.toByteArray());
        return null;
     }

       /**
     * Generates a csv file from experiments
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView getExperimentsCSVFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Controller for csv file with creating experiments for NIF");
        OutputStream out = null;
        int headerBufferSize = 8096;

        response.setHeader("Content-Type", "text/csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=EEGbase_experiments_example_2011.csv");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);

        log.debug("Generating csv");
        out = csvFactory.generateExperimentsCsvFile();
        ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
        response.getOutputStream().write(bout.toByteArray());
        return null;
     }
}
