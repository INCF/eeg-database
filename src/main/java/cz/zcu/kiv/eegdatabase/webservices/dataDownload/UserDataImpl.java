package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Web service providing user's data remotely.
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.dataDownload.UserDataService")
@SuppressWarnings("unchecked")
public class UserDataImpl implements UserDataService {

    /* necessary Dao objects*/
    private PersonDao personDao;
    private ExperimentDao experimentDao;
    private Log log = LogFactory.getLog(getClass());

    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public boolean isServiceAvailable() {

        log.debug("User " + personDao.getLoggedPerson().getUsername() +
                " verified connection with dataDownload web service.");
        return true;
    }

    public List<ExperimentInfo> getExperiments(Rights rights) {
        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        if (rights == Rights.ALL) {
            experiments = new LinkedList<Experiment>(experimentDao.getAllRecords());
        } else if (rights == Rights.SUBJECT)
            experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForSubjectPersonId());
        else
            experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForOwnerId());

        for (Experiment experiment : experiments) {

            ExperimentInfo info = new ExperimentInfo();
            info.setExperimentId(experiment.getExperimentId());
            info.setOwnerId(experiment.getPersonByOwnerId().getPersonId());
            info.setSubjectPersonId(experiment.getPersonBySubjectPersonId().getPersonId());
            info.setScenarioId(experiment.getScenario().getScenarioId());
            info.setStartTimeInMilis(experiment.getStartTime().getTime());
            info.setEndTimeInMilis(experiment.getEndTime().getTime());
            info.setWeatherId(experiment.getWeather().getWeatherId());
            info.setWeatherNote(experiment.getWeathernote());
            info.setPrivateFlag((experiment.isPrivateExperiment() ? 1 : 0));
            info.setResearchGroupId(experiment.getResearchGroup().getResearchGroupId());
            info.setTemperature(experiment.getTemperature());

            exps.add(info);
        }

        log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved experiment list with " + rights.toString() + " rights.");

        return exps;
    }

    public List<DataFileInfo> getExperimentFiles(int experimentId) throws DataDownloadException {
        List<DataFile> files = experimentDao.getDataFilesWhereExpId(experimentId);
        List<DataFileInfo> fileInfos = new LinkedList<DataFileInfo>();

        try {

            for (DataFile file : files) {

                DataFileInfo info = new DataFileInfo();

                info.setExperimentId(file.getExperiment().getExperimentId());
                info.setFileId(file.getDataFileId());
                info.setFileLength(file.getFileContent().length());
                info.setFileName(file.getFilename());
                info.setMimeType(file.getMimetype());
                info.setSamplingRate(file.getSamplingRate());

                fileInfos.add(info);
            }

            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved list of experiment " + experimentId + " data files.");
        } catch (SQLException e) {
            DataDownloadException exception = new DataDownloadException(e);
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve list of experiment " + experimentId + " data files!");
            log.error(e.getMessage(), e);
            throw exception;
        }

        return fileInfos;
    }

    public DataHandler downloadFile(int dataFileId) throws DataDownloadException {

        List<DataFile> files = experimentDao.getDataFilesWhereId(dataFileId);
        DataFile file = files.get(0);

        DataSource rawData = null;
        try {
            final InputStream in = file.getFileContent().getBinaryStream();
            rawData = new DataSource() {
                public String getContentType() {
                    return "application/octet-stream";
                }

                public InputStream getInputStream() throws IOException {
                    return in;
                }

                public String getName() {
                    return "application/octet-stream";
                }

                public OutputStream getOutputStream() throws IOException {
                    return null;
                }
            };
            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved file " + dataFileId);
        } catch (SQLException e) {
            DataDownloadException exception = new DataDownloadException(e);
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve file " + dataFileId);
            log.error(e.getMessage(), e);
            throw exception;
        }

        return new DataHandler(rawData);
    }


}