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
import cz.zcu.kiv.eegdatabase.logic.commandobjects.HistorySearcherCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author pbruha
 */
public class SearchHistoryController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private PersonDao personDao;
  private SimpleHistoryDao<History, Integer> historyDao;

  public SearchHistoryController() {
    setCommandClass(HistorySearcherCommand.class);
    setCommandName("historySearcherCommand");
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    HistorySearcherCommand search = (HistorySearcherCommand) super.formBackingObject(request);
    return search;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    log.debug("Processing advanced search download history");
    ModelAndView mav = new ModelAndView("history/searchResults");
    int userId;
    List<History> historyList = null;
    Person user = null;
    String authority = null;
    String role = "";
    String roleAdmin = "ROLE_ADMIN";
    boolean notAdmin;
    boolean isGroupAdmin;

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

      List<String> source = new ArrayList<String>();
      List<String> condition = new ArrayList<String>();
      List<String> andOr = new ArrayList<String>();
      Enumeration enumer = request.getParameterNames();
      while (enumer.hasMoreElements()) {
        String param = (String) enumer.nextElement();
        if (param.startsWith("source")) {
          source.add(param);
        } else if (param.startsWith("condition")) {
          condition.add(param);
        } else {
          andOr.add(param);
        }
      }
      Collections.sort(andOr);
      Collections.sort(source);
      Collections.sort(condition);
      List<SearchRequest> requests = new ArrayList<SearchRequest>();
      requests.add(new SearchRequest(request.getParameter(condition.get(0)),
              (request.getParameter(source.get(0))), ""));
      for (int i = 1; i < condition.size(); i++) {
        requests.add(new SearchRequest(request.getParameter(condition.get(i)),
                (request.getParameter(source.get(i))),
                (request.getParameter(andOr.get(i - 1)))));
      }
      try {
        List<History> historyResults = historyDao.getHistorySearchResults(requests,isGroupAdmin, groupsId);
        mav.addObject("historyResults", historyResults);
        mav.addObject("resultsEmpty", historyResults.isEmpty());
      } catch (NumberFormatException e) {
        mav.addObject("mistake", "Number error");
        mav.addObject("error", true);
        // System.out.println("Number error");
      } catch (RuntimeException e) {
        mav.addObject("mistake", e.getMessage());
        mav.addObject("error", true);
        //System.out.println(e.getMessage());
      }
      return mav;
    }
    mav.setViewName("system/accessDenied");
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
