package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * REST service controller for downloading/uploading datafiles
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/datafile")
public class DataFileServiceController {

    private final static Log log = LogFactory.getLog(DataFileServiceController.class);
    @Autowired
    DataFileService dataFileService;

    /**
     * Creates new DataFile under specified experiment.
     * In response is hidden url for file download.
     *
     * @param experimentId experiment identifier
     * @param description  data file description
     * @param file         data file multipart
     * @throws RestServiceException error while creating record
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(HttpServletRequest request, HttpServletResponse response, @RequestParam("experimentId") int experimentId, @RequestParam("description") String description, @RequestParam("file") MultipartFile file) throws RestServiceException {
        try {
            int pk = dataFileService.create(experimentId, description, file);
            response.addHeader("Location", buildLocation(request, pk));
            log.debug("File upload detected: " + file.getName());
        } catch (IOException e) {
            log.error("File upload failed: " + file.getName());
            throw new RestServiceException(e);
        }
    }

    /**
     * Method for downloading file from server.
     *
     * @param id       data file identifier
     * @param response HTTP response
     * @throws RestServiceException  error while accessing to file
     * @throws RestNotFoundException no such file on server
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") int id, HttpServletResponse response) throws RestServiceException, RestNotFoundException {
        try {
            dataFileService.getFile(id, response);
        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(ex);
        } catch (SQLException e) {
            log.error("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(e);
        }
    }

    /**
     * Builds URL for file download.
     *
     * @param request HTTP request
     * @param id      data file id
     * @return URL string
     */
    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{id}").toString());
        return ut.expand(id).toASCIIString();
    }

    /**
     * Exception handler for RestServiceException.class.
     * Writes exception message into HTTP response.
     *
     * @param ex       exception body
     * @param response HTTP response
     * @throws IOException error while writing into response
     */
    @ExceptionHandler(RestServiceException.class)
    public void handleException(RestServiceException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        log.error(ex);
    }

    /**
     * Exception handler for RestNotFoundException.class.
     * Writes exception message into HTTP response.
     *
     * @param ex       exception body
     * @param response HTTP response
     * @throws IOException error while writing into response
     */
    @ExceptionHandler(RestNotFoundException.class)
    public void handleException(RestNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        log.error(ex);
    }

}
