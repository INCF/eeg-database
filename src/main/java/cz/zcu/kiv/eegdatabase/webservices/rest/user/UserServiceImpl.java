package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ExperimentData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ResearchGroupData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of user service
 *
 * @author Petr Miko
 */
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    @Override
    public UserInfo login() {
        Person user = personDao.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }

    @Override
    public List<ResearchGroupData> getMyGroups() throws RestServiceException {

        try {
            Set<ResearchGroup> groups = personDao.getLoggedPerson().getResearchGroups();
            List<ResearchGroupData> data = new ArrayList<ResearchGroupData>(groups.size());

            for (ResearchGroup g : groups) {
                ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
                data.add(d);
            }

            return data;
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }

    @Override
    public List<ExperimentData> getMyExperiments() throws RestServiceException {
        try {

            Set<Experiment> experiments = personDao.getLoggedPerson().getExperiments();
            List<ExperimentData> data = new ArrayList<ExperimentData>(experiments.size());

            for(Experiment exp : experiments){
                ExperimentData expData = new ExperimentData();
                expData.setExperimentId(exp.getExperimentId());
                expData.setStartTime(exp.getStartTime());
                expData.setEndTime(exp.getEndTime());
                expData.setScenarioId(exp.getScenario().getScenarioId());
                expData.setScenarioName(exp.getScenario().getScenarioName());
                data.add(expData);
            }
            return data;
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
