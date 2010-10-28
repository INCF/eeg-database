/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleHistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author pbruha
 */
public class MonthlyHistoryController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private SimpleHistoryDao<History, Integer> historyDao;
  private AuthorizationManager auth;
  private PersonDao personDao;

  public MonthlyHistoryController() {
  }

  protected ModelAndView handleRequestInternal(
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
    log.debug("Processing monthly download history");
    ModelAndView mav = new ModelAndView("history/monthlyHistory");
    String countOfDownloadedFiles;
    int userId;
    Person user = null;
    String authority = null;
    String roleAdmin = "ROLE_ADMIN";
    boolean isGroupAdmin;
    List<History> historyList = null;
    List<History> lastDownloadedFilesHistoryList = null;
    List<DownloadStatistic> topDownloadedFilesList = null;
    userId = personDao.getLoggedPerson().getPersonId();
    user = personDao.getPerson(ControllerUtils.getLoggedUserName());
    authority = user.getAuthority();
    isGroupAdmin = auth.userIsGroupAdmin();
    if (authority.equals(roleAdmin) || isGroupAdmin) {
      if (authority.equals(roleAdmin)) {
        isGroupAdmin = false;
      }
      Set<ResearchGroupMembership> rgm = user.getResearchGroupMemberships();
      List<Integer> groupsId = new ArrayList<Integer>();

      for (ResearchGroupMembership member : rgm) {
        if (member.getAuthority().equals("GROUP_ADMIN")) {
          groupsId.add(member.getResearchGroup().getResearchGroupId());
        }
      }
      historyList = historyDao.getHistory(SimpleHistoryDao.Choice.MONTHLY.toString(), isGroupAdmin, userId,groupsId);
      lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory(SimpleHistoryDao.Choice.MONTHLY.toString(), isGroupAdmin, groupsId);
      topDownloadedFilesList = historyDao.getTopDownloadHistory(SimpleHistoryDao.Choice.MONTHLY.toString(), isGroupAdmin, groupsId);

      countOfDownloadedFiles = "" + historyList.size();
      mav.addObject("countOfDownloadedFiles", countOfDownloadedFiles);
      mav.addObject("historyList", historyList);
      mav.addObject("topDownloadedFilesList", topDownloadedFilesList);
      mav.addObject("lastDownloadedFilesHistoryList", lastDownloadedFilesHistoryList);
      return mav;
    }
    mav.setViewName("system/accessDeniedNotAdmin");
    return mav;
  }

  public SimpleHistoryDao<History, Integer> getHistoryDao() {
    return historyDao;
  }

  public void setHistoryDao(SimpleHistoryDao<History, Integer> historyDao) {
    this.historyDao = historyDao;
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
}
