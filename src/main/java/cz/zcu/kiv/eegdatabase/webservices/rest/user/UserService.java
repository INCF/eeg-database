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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of user service
 *
 * @author Petr Miko
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/user")
public class UserService {

    private final static Log log = LogFactory.getLog(UserService.class);

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public UserInfo login(){
        Person user = personDao.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }

    @RequestMapping(value="/groups", method = RequestMethod.GET)
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

    @RequestMapping(value="/experiments", method = RequestMethod.GET)
    public List<ExperimentData> getMyExperiments()  throws RestServiceException {
        try {

            Set<Experiment> exps = personDao.getLoggedPerson().getExperiments();
            List<ExperimentData> experiments = new ArrayList<ExperimentData>(exps.size());

            for(Experiment exp : exps){
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

    @ExceptionHandler(RestServiceException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "REST service error occurred")
    public String handleRSException(RestServiceException ex) {
        log.error(ex);
        return ex.getMessage();
    }
}
