package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
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

    public boolean serviceAvailable() {

        return true;
    }

    public List<ExperimentInfo> availableExperiments(Rights rights){
        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        if(rights == Rights.MEMBER)
                experiments = experimentDao.getExperimentsWhereMember(personDao.getLoggedPerson().getPersonId());
            else
                experiments = experimentDao.getExperimentsWhereOwner(personDao.getLoggedPerson().getPersonId());

        for (Experiment experiment : experiments) {
            exps.add(new ExperimentInfo(experiment.getExperimentId(),experiment.getScenario().getTitle()));
        }

        return exps;
    }

}

    
