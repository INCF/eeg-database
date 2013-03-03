package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Service
public class DataFileServiceImpl implements DataFileService {

    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;
    @Autowired
    private DataFileDao dataFileDao;

    @Override
    @Transactional
    public int create(int experimentId, String description, MultipartFile file) throws IOException {
        DataFile datafile = new DataFile();
        datafile.setExperiment(experimentDao.getExperimentForDetail(experimentId));
        datafile.setDescription(description);
        datafile.setFileContent(Hibernate.createBlob(file.getInputStream()));
        return dataFileDao.create(datafile);
    }

    @Override
    @Transactional(readOnly = true)
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, RestNotFoundException {
        List<DataFile> expsForId = experimentDao.getDataFilesWhereId(id);
        DataFile file = null;

        if (expsForId != null && !expsForId.isEmpty()) {
            file = expsForId.get(0);
        }

        if (file == null) throw new RestNotFoundException("No file with such id!");
        InputStream is = file.getFileContent().getBinaryStream();
        // copy it to response's OutputStream
        response.setContentType(file.getMimetype());
        response.setContentLength((int) file.getFileContent().length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }
}
