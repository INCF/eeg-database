package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioTypeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.Paginator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author Jindra
 */
public class ScenarioMultiController extends MultiActionController {

    private AuthorizationManager auth;
    private ScenarioDao scenarioDao;
    private ScenarioTypeDao scenarioTypeDao;
    private PersonDao personDao;

    private static final int ITEMS_PER_PAGE = 20;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("scenario/list");
        Person loggedUser = personDao.getLoggedPerson();

        Paginator paginator = new Paginator(scenarioDao.getScenarioCountForList(loggedUser), ITEMS_PER_PAGE);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }
        paginator.setActualPage(page);
        mav.addObject("paginator", paginator.getLinks());
        List<Scenario> list = scenarioDao.getScenariosForList(loggedUser, paginator.getFirstItemIndex(), ITEMS_PER_PAGE);
        mav.addObject("scenarioList", list);
        return mav;
    }

    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("scenario/detail");
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("scenarioId"));
        } catch (Exception e) {
        }
        Scenario scenario = scenarioDao.read(id);
        IScenarioType scenarioType = scenario.getScenarioType();
        mav.addObject("scenarioDetail", scenario);
        mav.addObject("isOwner", (auth.userIsOwnerOfScenario(id))||(auth.isAdmin()));
        mav.addObject("containsFile", scenarioType.getScenarioXml() != null);

        return mav;
    }

    public ModelAndView myScenarios(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("scenario/myScenarios");
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());
        List<Scenario> list = scenarioDao.getScenariosWhereOwner(personDao.getLoggedPerson());
        mav.addObject("scenarioList", list);
        return mav;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ScenarioDao getScenarioDao() {
        return scenarioDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }
}
