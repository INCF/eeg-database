package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
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

    public ExperimentDao getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    /**
     * Method just for checking web service availability.
     * (user needs to connect through Spring security but doesn't wish to do anything more)
     *
     * @return true
     */
    public boolean isServiceAvailable() {
        log.debug("User " + personDao.getLoggedPerson().getUsername() +
                " verified connection with dataDownload web service.");
        return true;
    }

    /**
     * Method returning List of information about available experiments.
     *
     * @param rights defines rights that user has in desired experiments (user, subject)
     * @return List of information about available experiments
     */
    public List<ExperimentInfo> getExperiments(Rights rights) {
        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        if (rights == Rights.SUBJECT)
            experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForSubjectPersonId());
        else
            experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForOwnerId());

        for (Experiment experiment : experiments) {
            exps.add(new ExperimentInfo(experiment.getExperimentId(), experiment.getScenario().getScenarioId(),
                    experiment.getScenario().getTitle()));
        }

        log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved experiment list with " + rights.toString() + " rights.");

        return exps;
    }

    /**
     * Method returning list of files, which belong to experiment defined by id.
     *
     * @param experimentId Number defining explored experiment
     * @return List of information about experiment's data files
     * @throws WebServiceException wrapped SQLException
     */
    public List<DataFileInfo> getExperimentFiles(int experimentId) throws WebServiceException {
        List<DataFile> files = experimentDao.getDataFilesWhereExpId(experimentId);
        List<DataFileInfo> fileInfos = new LinkedList<DataFileInfo>();

        try {

            for (DataFile file : files) {
                fileInfos.add(new DataFileInfo(experimentId, file.getExperiment().getScenario().getTitle(),
                        file.getDataFileId(), file.getFilename(), file.getMimetype(), file.getFileContent().length()));
            }

            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved list of experiment " + experimentId + " data files.");
        } catch (SQLException e) {
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve list of experiment " + experimentId + " data files!");
            log.error(e);
            throw new WebServiceException(e);
        }

        return fileInfos;
    }

    /**
     * Method streaming desired file back to user.
     *
     * @param dataFileId Id of file to download
     * @return Stream of bytes (file)
     * @throws WebServiceException Wrapped SQLException and IOException
     */
    public DataHandler downloadFile(int dataFileId) throws WebServiceException {

        List<DataFile> files = experimentDao.getDataFilesWhereId(dataFileId);
        DataFile file = files.get(0);

        ByteArrayDataSource rawData = null;
        try {
            rawData = new ByteArrayDataSource(file.getFileContent().getBinaryStream(), "fileBinaryStream");
            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved file " + dataFileId);
        } catch (IOException e) {
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve file " + dataFileId);
            log.error(e);
            throw new WebServiceException(e);
        } catch (SQLException e) {
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve file " + dataFileId);
            log.error(e);
            throw new WebServiceException(e);
        }

        return new DataHandler(rawData);
    }


}