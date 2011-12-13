package cz.zcu.kiv.eegdatabase.logic.controller.nif;

import cz.zcu.kiv.eegdatabase.logic.csv.CSVFactory;
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
 * Created by IntelliJ IDEA.
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
     * Generates a csv file from experiments
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     public ModelAndView getCSVFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
                   log.debug("Controller for creating csv file for NIF");
        OutputStream out = null;
        int headerBufferSize = 8096;

        response.setHeader("Content-Type", "text/csv");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=eegdatabase.csv");

        log.debug("Creating output stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(headerBufferSize);

        log.debug("Generating csv");
        out = csvFactory.generateCsvFile();
        ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
        response.getOutputStream().write(bout.toByteArray());
        return null;
     }
}
