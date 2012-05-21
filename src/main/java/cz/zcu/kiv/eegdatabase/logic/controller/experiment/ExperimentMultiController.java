package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ServiceResult;
import cz.zcu.kiv.eegdatabase.logic.util.Paginator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    private static final int ITEMS_PER_PAGE = 20;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");

        Person loggedUser = personDao.getLoggedPerson();
        setPermissionsToView(mav);
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());
        Paginator paginator = new Paginator(experimentDao.getCountForAllExperimentsForUser(loggedUser), ITEMS_PER_PAGE);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
        }
        paginator.setActualPage(page);
        List experimentList = experimentDao.getAllExperimentsForUser(loggedUser, paginator.getFirstItemIndex(), ITEMS_PER_PAGE);
        mav.addObject("paginator", paginator.getLinks());
        boolean userNotMemberOfAnyGroup = researchGroupDao.getResearchGroupsWhereMember(loggedUser, 1).isEmpty();

        mav.addObject("experimentListTitle", "pageTitle.allExperiments");
        mav.addObject("experimentList", experimentList);
        mav.addObject("userNotMemberOfAnyGroup", userNotMemberOfAnyGroup);
        return mav;
    }

    public ModelAndView myExperiments(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");

        Person loggedUser = personDao.getLoggedPerson();
        setPermissionsToView(mav);
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());
        Paginator paginator = new Paginator(experimentDao.getCountForExperimentsWhereOwner(loggedUser), ITEMS_PER_PAGE);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
        }
        paginator.setActualPage(page);
        List experimentList = experimentDao.getExperimentsWhereOwner(loggedUser, paginator.getFirstItemIndex(), ITEMS_PER_PAGE);
        mav.addObject("paginator", paginator.getLinks());
        boolean userNotMemberOfAnyGroup = researchGroupDao.getResearchGroupsWhereMember(loggedUser, 1).isEmpty();

        mav.addObject("experimentListTitle", "pageTitle.myExperiments");
        mav.addObject("experimentList", experimentList);
        mav.addObject("userNotMemberOfAnyGroup", userNotMemberOfAnyGroup);
        return mav;
    }

    public ModelAndView meAsSubject(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("experiments/list");

        Person loggedUser = personDao.getLoggedPerson();
        setPermissionsToView(mav);
        log.debug("Logged user ID from database is: " + loggedUser.getPersonId());
        Paginator paginator = new Paginator(experimentDao.getCountForExperimentsWhereSubject(loggedUser), ITEMS_PER_PAGE);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
        }
        paginator.setActualPage(page);
        List experimentList = experimentDao.getExperimentsWhereSubject(loggedUser, paginator.getFirstItemIndex(), ITEMS_PER_PAGE);
        mav.addObject("paginator", paginator.getLinks());
        boolean userNotMemberOfAnyGroup = researchGroupDao.getResearchGroupsWhereMember(loggedUser, 1).isEmpty();

        mav.addObject("experimentListTitle", "pageTitle.myExperiments");
        mav.addObject("experimentList", experimentList);
        mav.addObject("userNotMemberOfAnyGroup", userNotMemberOfAnyGroup);
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
        Experiment m = experimentDao.getExperimentForDetail(id);

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
        } else {
            response.setHeader("Content-Type", "application/png");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + service.getFilename());
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
