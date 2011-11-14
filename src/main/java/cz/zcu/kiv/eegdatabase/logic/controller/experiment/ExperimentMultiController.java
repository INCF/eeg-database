package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Delegate class for multicontroller for experiments.
 *
 * @author Jindra
 */
public class ExperimentMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private PersonDao personDao;
    private ExperimentDao<Experiment, Integer> experimentDao;
    private ServiceResultDao resultDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");
        setPermissionsToView(mav);

        Person loggedUser = personDao.getLoggedPerson();
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());
        List<Experiment> list = experimentDao.getAllExperimentsForUser(loggedUser.getPersonId());
        mav.addObject("measurationListTitle", "pageTitle.allExperiments");
        mav.addObject("measurationList", list);

        return mav;
    }

    public ModelAndView myExperiments(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");

        setPermissionsToView(mav);

        Person loggedUser = personDao.getLoggedPerson();
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());

        List<Experiment> list = experimentDao.getExperimentsWhereOwner(loggedUser.getPersonId());
        mav.addObject("measurationListTitle", "pageTitle.myExperiments");
        mav.addObject("measurationList", list);

        return mav;
    }

    public ModelAndView meAsSubject(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");

        setPermissionsToView(mav);

        Person loggedUser = personDao.getLoggedPerson();
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());

        List<Experiment> list = experimentDao.getExperimentsWhereSubject(loggedUser.getPersonId());
        mav.addObject("measurationListTitle", "pageTitle.meAsSubject");
        mav.addObject("measurationList", list);

        return mav;
    }

    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/detail");

        setPermissionsToView(mav);

        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("experimentId"));
        } catch (Exception e) {
        }
        Experiment m = experimentDao.read(id);

        mav.addObject("userIsOwnerOrCoexperimenter", (auth.userIsOwnerOrCoexperimenter(id)) || (auth.isAdmin()));
        int subjectPersonId = m.getPersonBySubjectPersonId().getPersonId();
        mav.addObject("userCanViewPersonDetails", auth.userCanViewPersonDetails(subjectPersonId));

        mav.addObject("experimentDetail", m);

        return mav;
    }

    private void setPermissionsToView(ModelAndView mav) {
        boolean userIsExperimenter = auth.userIsExperimenter();
        mav.addObject("userIsExperimenter", userIsExperimenter);
    }

    public ModelAndView servicesResult(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("services/results");
        List<ServiceResult> results = resultDao.getResultByPerson(personDao.getLoggedPerson().getPersonId());
        mav.addObject("results", results);
        mav.addObject("resultsEmpty", results.isEmpty());
        return mav;
    }

    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) {
        return  new ModelAndView("services/results");
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        return  new ModelAndView("services/results");
    }

    public ExperimentDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(ExperimentDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public ServiceResultDao getResultDao() {
        return resultDao;
    }

    public void setResultDao(ServiceResultDao resultDao) {
        this.resultDao = resultDao;
    }
}
