package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

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

    private final static Log logger = LogFactory.getLog(DataFileServiceController.class);
    @Autowired
    DataFileService dataFileService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(HttpServletRequest request, HttpServletResponse response, @RequestParam("experimentId") int experimentId, @RequestParam("description") String description, @RequestParam("file") MultipartFile file) throws RestServiceException {
        try {
            int pk = dataFileService.create(experimentId, description, file);
            response.addHeader("Location", buildLocation(request, pk));
            logger.debug("File upload detected: " + file.getName());
        } catch (IOException e) {
            logger.error("File upload failed: " + file.getName());
            throw new RestServiceException(e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") int id, HttpServletResponse response) throws RestServiceException {
        try {
            dataFileService.getFile(id, response);
        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(ex);
        } catch (SQLException e) {
            logger.info("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException(e);
        }
    }

    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{id}").toString());
        return ut.expand(id).toASCIIString();
    }

}
