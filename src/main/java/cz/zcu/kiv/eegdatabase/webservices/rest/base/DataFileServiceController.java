package cz.zcu.kiv.eegdatabase.webservices.rest.base;

import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * REST service controller for downloading/uploading datafiles
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/base/datafile")
public class DataFileServiceController {

    private final static Log logger = LogFactory.getLog(DataFileServiceController.class);
    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;

    @Autowired
    private DataFileDao dataFileDao;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(HttpServletRequest request, HttpServletResponse response, @RequestParam("experimentId") int experimentId, @RequestParam("description") String description, @RequestParam("file") MultipartFile file) throws RestServiceException {

        DataFile datafile = new DataFile();
        try {
            datafile.setExperiment(experimentDao.getExperimentForDetail(experimentId));
            datafile.setDescription(description);
            datafile.setFileContent(Hibernate.createBlob(file.getInputStream()));
            int pk = dataFileDao.create(datafile);
            response.addHeader("Location", buildLocation(request, pk));
            logger.debug("File upload detected: " + file.getName());
        } catch (IOException e) {
            logger.error("File upload failed: " + file.getName());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") int id, HttpServletResponse response) throws RestServiceException {
        try {

            List<DataFile> expsForId = experimentDao.getDataFilesWhereId(id);
            if (expsForId != null && !expsForId.isEmpty()) {
                DataFile file = expsForId.get(0);
                InputStream is = file.getFileContent().getBinaryStream();
                // copy it to response's OutputStream
                response.setContentType(file.getMimetype());
                response.setContentLength((int) file.getFileContent().length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            }
        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException("IOError writing file to output stream");
        } catch (SQLException e) {
            logger.info("Error writing file to output stream. Filename was '" + id + "'");
            throw new RestServiceException("IOError writing file to output stream");
        }
    }

    private String buildLocation(HttpServletRequest request, Object id) {
        StringBuffer url = request.getRequestURL();
        UriTemplate ut = new UriTemplate(url.append("/{id}").toString());
        return ut.expand(id).toASCIIString();
    }

}
