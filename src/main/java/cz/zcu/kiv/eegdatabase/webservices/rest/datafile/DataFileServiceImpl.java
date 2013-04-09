package cz.zcu.kiv.eegdatabase.webservices.rest.datafile;

import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
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
 * Service implementation for data file type.
 *
 * @author Petr Miko
 */
@Service
public class DataFileServiceImpl implements DataFileService {

    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;
    @Autowired
    @Qualifier("dataFileDao")
    private DataFileDao dataFileDao;
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao researchGroupDao;

    /**
     * Creates new DataFile under specified experiment.
     *
     * @param experimentId experiment identifier
     * @param description  data file description
     * @param file         data file multipart
     * @return identifier of created record
     * @throws IOException error while creating record
     */
    @Override
    @Transactional
    public int create(int experimentId, String description, MultipartFile file) throws IOException {
        DataFile datafile = new DataFile();
        datafile.setExperiment(experimentDao.getExperimentForDetail(experimentId));
        datafile.setDescription(description);
        datafile.setFilename(file.getOriginalFilename().replace(" ", "_"));
        datafile.setFileContent(Hibernate.createBlob(file.getInputStream()));
        datafile.setMimetype(file.getContentType() != null ? file.getContentType().toLowerCase() : "application/octet-stream");

        //DB column size restriction
        if(datafile.getMimetype().length() > 40){
            datafile.setMimetype("application/octet-stream");
        }
        return dataFileDao.create(datafile);
    }

    /**
     * Method for downloading file from server.
     *
     * @param id       data file identifier
     * @param response HTTP response
     * @throws RestServiceException  error while accessing to file
     * @throws SQLException          error while reading file from db
     * @throws IOException           error while reading file
     * @throws RestNotFoundException no such file on server
     */
    @Override
    @Transactional(readOnly = true)
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, RestNotFoundException {
        List<DataFile> dataFiles = experimentDao.getDataFilesWhereId(id);
        DataFile file = null;

        if (dataFiles != null && !dataFiles.isEmpty()) {
            file = dataFiles.get(0);
        }


        if (file == null) throw new RestNotFoundException("No file with such id!");

        //if is user member of group, then he has rights to download file
        //basic verification, in future should be extended
        ResearchGroup expGroup = file.getExperiment().getResearchGroup();
        if (!isInGroup(personDao.getLoggedPerson(), expGroup.getResearchGroupId())) throw new RestServiceException("User does not have access to this file!");

        InputStream is = file.getFileContent().getBinaryStream();
        // copy it to response's OutputStream
        response.setContentType(file.getMimetype());
        response.setContentLength((int) file.getFileContent().length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * Checks if is user a member of specified research group.
     *
     * @param user            user
     * @param researchGroupId research group identifier
     * @return true if is user in group
     */
    private boolean isInGroup(Person user, int researchGroupId) {
        if (!user.getResearchGroupMemberships().isEmpty()) {
            for (ResearchGroup g : researchGroupDao.getResearchGroupsWhereMember(user)) {
                if (g.getResearchGroupId() == researchGroupId) {
                    return true;
                }
            }
        }
        return false;
    }
}
