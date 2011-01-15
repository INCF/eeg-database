package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author Jindra
 */
public class ScenarioMultiController extends MultiActionController {

  private AuthorizationManager auth;
  private ScenarioDao scenarioDao;
  private PersonDao personDao;

  public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView("scenario/list");
    mav.addObject("userIsExperimenter", auth.userIsExperimenter());
    List<Scenario> list = scenarioDao.getAllRecords();
    Person loggedUser = personDao.getLoggedPerson();
    
    int groupId;
    for (Scenario item:list)
    {
      groupId = (int)item.getResearchGroup().getResearchGroupId();
      Set<ResearchGroupMembership> researchGroupMemberships = item.getResearchGroup().getResearchGroupMemberships();
      boolean userIsMember = false;
      for (ResearchGroupMembership member:researchGroupMemberships)
      {
        if (member.getPerson().getPersonId() == loggedUser.getPersonId())
        {
          userIsMember = true;
          break;
        }
      }
      item.setUserMemberOfGroup(userIsMember);
    }

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
    mav.addObject("scenarioDetail", scenario);

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
