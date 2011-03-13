package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.dataDownload.UserDataService")
@SuppressWarnings("unchecked")
public class UserDataImpl implements UserDataService {

    /* necessary Dao objects*/
    private PersonDao personDao;
    private ExperimentDao experimentDao;

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

    /* Method just for checking web service availability */
    public boolean isServiceAvailable() {
        return true;
    }

    /* Method returning list of experiments' information according to desired rights */
    public List<ExperimentInfo> getAvailableExperimentsWithRights(Rights rights){
        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        if(rights == Rights.SUBJECT)
                experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForSubjectPersonId());
            else
                experiments = new LinkedList<Experiment>(personDao.getLoggedPerson().getExperimentsForOwnerId());

        for (Experiment experiment : experiments) {
            exps.add(new ExperimentInfo(experiment.getExperimentId(),experiment.getScenario().getTitle()));
        }

        return exps;
    }

    /* Method returning list containing information about data files selected by experiment id */
    public List<DataFileInfo> getExperimentDataFilesWhereExpId(int experimentId){
        List<DataFile> files = experimentDao.getDataFilesWhereExpId(experimentId);
        List<DataFileInfo> fileInfos = new LinkedList<DataFileInfo>();

        for (DataFile file : files) {
            fileInfos.add(new DataFileInfo(file.getDataFileId(),file.getFilename(),file.getMimetype()));
        }

        return fileInfos;
    }

    /* Method returning DataHandler object which contains data file binary. Data file is selected by id */
    public DataHandler getDataFileBinaryWhereFileId(int dataFileId) {

        List<DataFile> files = experimentDao.getDataFilesWhereId(dataFileId);
        DataFile file = files.get(0);

        ByteArrayDataSource rawData= null;
        try {
            rawData = new ByteArrayDataSource(file.getFileContent().getBinaryStream(),"fileBinaryStream");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new DataHandler(rawData);
    }


}