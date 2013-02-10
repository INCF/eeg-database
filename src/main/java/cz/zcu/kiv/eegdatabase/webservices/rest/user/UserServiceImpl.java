package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ExperimentData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ResearchGroupData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ResearchGroupDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Petr Miko
 *         Date: 10.2.13
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    @Override
    @Transactional(readOnly = true)
    public UserInfo login() {
        Person user = personDao.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchGroupDataList getMyGroups() throws RestServiceException {

        try {
            Set<ResearchGroup> groups = personDao.getLoggedPerson().getResearchGroups();
            List<ResearchGroupData> list = new ArrayList<ResearchGroupData>();

            for (ResearchGroup g : groups) {
                ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
                list.add(d);
            }
            return new ResearchGroupDataList(list);
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentData> getMyExperiments() throws RestServiceException {
        try {

            Set<Experiment> exps = personDao.getLoggedPerson().getExperiments();
            List<ExperimentData> experiments = new ArrayList<ExperimentData>(exps.size());

            for (Experiment exp : exps) {
                ExperimentData expData = new ExperimentData();
                expData.setExperimentId(exp.getExperimentId());
                expData.setStartTime(exp.getStartTime());
                expData.setEndTime(exp.getEndTime());
                expData.setScenarioId(exp.getScenario().getScenarioId());
                expData.setScenarioName(exp.getScenario().getScenarioName());
                experiments.add(expData);
            }
            return experiments;
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
