package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;

import javax.jws.WebService;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.dataDownload.UserDataService")
public class UserDataImpl implements UserDataService {

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

    public boolean isServiceAvailable() {
        return true;
    }

    public List<ExperimentInfo> getAvailableExperiments(Rights rights){
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

    public List<DataFileInfo> getExperimentDataFiles(int experimentID){
        List<DataFile> files = experimentDao.getDataFilesWhereExpId(experimentID);
        List<DataFileInfo> fileInfos = new LinkedList<DataFileInfo>();

        for (DataFile file : files) {
            fileInfos.add(new DataFileInfo(file.getDataFileId(),file.getFilename(),file.getMimetype()));
        }

        return fileInfos;
    }
}