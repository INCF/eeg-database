package cz.zcu.kiv.eegdatabase.logic.delegate;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Delegate class for multicontroller which processes groups section.
 *
 * @author Jindrich Pergler
 */
public class HomePageDelegate {

  private static final int LIMIT = 5;
  private ExperimentDao experimentDao;
  private ScenarioDao scenarioDao;
  private ResearchGroupDao researchGroupDao;
  private PersonDao personDao;
  private ArticleDao articleDao;
  private AuthorizationManager auth;
  /** Visible articles count constant */
  private int ARTICLES_COUNT = 10;

  public ExperimentDao getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(ExperimentDao experimentDao) {
    this.experimentDao = experimentDao;
  }

  public ScenarioDao getScenarioDao() {
    return scenarioDao;
  }

  public void setScenarioDao(ScenarioDao scenarioDao) {
    this.scenarioDao = scenarioDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ArticleDao getArticleDao() {
    return articleDao;
  }

  public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }

  public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView("homePage/homePage");

    Person person = personDao.getLoggedPerson();
    if (person != null) {
//      List<Experiment> myExperiments = experimentDao.getExperimentsWhereOwner(person, LIMIT);
//      myExperiments.clear();
//      int defaultGroupId = 0;
//      if (person.getDefaultGroup() != null) {
////        myExperiments = experimentDao.getExperimentsWhereMember(person.getDefaultGroup().getResearchGroupId(), LIMIT);
//        defaultGroupId = person.getDefaultGroup().getResearchGroupId();
//      }
//      Set<ResearchGroupMembership> rgm = person.getResearchGroupMemberships();
//      int groupId;
//      List<Experiment> groupExp;
//      for (ResearchGroupMembership groupMember : rgm) {
//        groupId = groupMember.getResearchGroup().getResearchGroupId();
//        if (groupId != defaultGroupId) {
//          groupExp = experimentDao.getExperimentsWhereMember(groupId);
//          myExperiments.addAll(groupExp);
//        }
//      }
      List myExperiments = experimentDao.getExperimentsWhereOwner(person, LIMIT);
      mav.addObject("myExperiments", myExperiments);
      mav.addObject("myExperimentsEmpty", myExperiments.isEmpty());

      List meAsSubjectList = experimentDao.getExperimentsWhereSubject(person, LIMIT);
      mav.addObject("meAsSubjectList", meAsSubjectList);
      mav.addObject("meAsSubjectListEmpty", meAsSubjectList.isEmpty());

      List myScenarios = scenarioDao.getScenariosWhereOwner(person, LIMIT);
      mav.addObject("myScenarios", myScenarios);
      mav.addObject("myScenariosEmpty", myScenarios.isEmpty());

      List<ResearchGroup> list = researchGroupDao.getResearchGroupsWhereMember(
              personDao.getLoggedPerson(), LIMIT);
      mav.addObject("groupList", list);
      mav.addObject("groupListEmpty", list.isEmpty());

      /* articles */

      Person loggedUser = personDao.getLoggedPerson();
      List<Article> articleList = articleDao.getAllRecords();

      for (Article article : articleList) {
        article.setUserMemberOfGroup(canView(loggedUser, article));
        if (--ARTICLES_COUNT == 0) {
          break;
        }
      }
      mav.addObject("articleList", articleList);
    }
    return mav;
  }

  /**
   * Determines if the logged user can view the supposed article
   * @param loggedUser Person object (Usually logged user)
   * @param article Article object
   * @return true if admin or member of group
   */
  public boolean canView(Person loggedUser, Article article) {
    if (loggedUser.getAuthority().equals("ROLE_ADMIN") || article.getResearchGroup() == null) {
      return true;
    }
    Set<ResearchGroupMembership> researchGroupMemberships = article.getResearchGroup().getResearchGroupMemberships();
    for (ResearchGroupMembership member : researchGroupMemberships) {
      if (member.getPerson().getPersonId() == loggedUser.getPersonId()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if user is admin in any group
   * @param mav ModelAndView for display
   */
  public void setPermissionsToView(ModelAndView mav) {
    // isAdmin
    Person loggedUser = personDao.getLoggedPerson();
    if (loggedUser.getAuthority().equals("ROLE_ADMIN")) {
      mav.addObject("userIsAdminInAnyGroup", true);
      return;
    }
    // check all groups for admin role
    Set<ResearchGroupMembership> researchGroupMemberShips = loggedUser.getResearchGroupMemberships();
    for (ResearchGroupMembership member : researchGroupMemberShips) {
      if (auth.userIsAdminInGroup(member.getResearchGroup().getResearchGroupId())) {
        mav.addObject("userIsAdminInAnyGroup", true);
        return;
      }
    }
  }
}
