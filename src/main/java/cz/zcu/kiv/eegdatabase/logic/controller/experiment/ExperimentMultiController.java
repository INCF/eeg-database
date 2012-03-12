package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Provider;
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
    private ResearchGroupDao researchGroupDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");
        setPermissionsToView(mav);

        Person loggedUser = personDao.getLoggedPerson();
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());
        List<Experiment> list = experimentDao.getAllExperimentsForUser(loggedUser.getPersonId());
        boolean userNotMemberOfAnyGroup = researchGroupDao.getResearchGroupsWhereMember(loggedUser,1).isEmpty();

        mav.addObject("measurationListTitle", "pageTitle.allExperiments");
        mav.addObject("measurationList", list);
        mav.addObject("userNotMemberOfAnyGroup", userNotMemberOfAnyGroup);
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

    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("serviceId"));

        } catch (Exception e) {
        }
        ServiceResult service = resultDao.read(id);
        if (service.getFilename().endsWith(".txt")) {
            response.setHeader("Content-Type", "plain/text");
        }
        else {
            response.setHeader("Content-Type", "application/png");
        }
        response.setHeader("Content-Disposition", "attachment;filename="+service.getFilename());
        response.getOutputStream().write(service.getFigure().getBytes(1, (int) service.getFigure().length()));
        //return new ModelAndView("redirect:services-result.html");
        return null;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("redirect:services-result.html");
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("serviceId"));

        } catch (Exception e) {
        }
        ServiceResult service = resultDao.read(id);
        resultDao.delete(service);
        return mav;
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

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }
}
