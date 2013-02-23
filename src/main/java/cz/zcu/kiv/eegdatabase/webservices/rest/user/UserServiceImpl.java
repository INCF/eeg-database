package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Petr Miko
 *         Date: 10.2.13
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Log log = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    @Qualifier("personService")
    @Autowired
    private PersonService personService;

    @Qualifier("mailService")
    @Autowired
    private MailService mailService;

    @Override
    public UserInfo create(AddPersonCommand cmd, Locale locale) throws RestServiceException {
        try {
            Person person = personService.createPerson(cmd);
            mailService.sendRegistrationConfirmMail(person, locale);
            return new UserInfo(person.getGivenname(), person.getSurname(), person.getAuthority());
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
            throw new RestServiceException(e);
        }
    }

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
    public ExperimentDataList getMyExperiments() throws RestServiceException {
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
            return new ExperimentDataList(experiments);
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
