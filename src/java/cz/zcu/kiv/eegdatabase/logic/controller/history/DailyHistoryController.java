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
public class DailyHistoryController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private PersonDao personDao;
  private SimpleHistoryDao<History, Integer> historyDao;

  public DailyHistoryController() {
    //Initialize controller properties here or
    //in the Web Application Context
    //setCommandClass(MyCommand.class);
    //setCommandName("MyCommandName");
    //setSuccessView("successView");
    //setFormView("formView");
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
    log.debug("Processing daily download history");
    String countOfDownloadedFiles;
    int userId;
    List<History> historyList = null;
    Person user = null;
    String authority = null;
    String roleAdmin = "ROLE_ADMIN";
    boolean isGroupAdmin;
    List<History> lastDownloadedFilesHistoryList = null;
    List<DownloadStatistic> topDownloadedFilesList = null;
    String historyType = "daily";
    userId = personDao.getLoggedPerson().getPersonId();
    ModelAndView mav = new ModelAndView("history/dailyHistory");
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

      historyList = historyDao.getHistory(historyType, isGroupAdmin, userId, groupsId);
      lastDownloadedFilesHistoryList = historyDao.getLastDownloadHistory(historyType, isGroupAdmin, groupsId);
      topDownloadedFilesList = historyDao.getTopDownloadHistory(historyType, isGroupAdmin, groupsId);

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
}
