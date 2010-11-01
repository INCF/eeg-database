/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HistoryDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ChangeDefaultGroupCommand;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author pbruha
 */
public class MonthlyHistoryController extends AbstractHistoryController {

  private Log log = LogFactory.getLog(getClass());
  private HistoryDao<History, Integer> historyDao;
  private AuthorizationManager auth;
  private PersonDao personDao;
  private ResearchGroupDao researchGroupDao;

  public MonthlyHistoryController() {
    setCommandClass(ChangeDefaultGroupCommand.class);
    setCommandName("changeDefaultGroup");
  }

   @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    log.debug("Processing daily download history");
    ModelAndView mav = new ModelAndView(getSuccessView());
    ChangeDefaultGroupCommand changeDefaultGroupCommand = (ChangeDefaultGroupCommand) command;
    mav=super.onSubmit(Choice.MONTHLY,changeDefaultGroupCommand, mav);
    return mav;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    log.debug("Processing daily download history");
    Map map = new HashMap<String, Object>();
    super.setDao(researchGroupDao, historyDao, auth, personDao);
    map = super.setReferenceData(map, Choice.MONTHLY);
    return map;
  }

  public HistoryDao<History, Integer> getHistoryDao() {
    return historyDao;
  }

  public void setHistoryDao(HistoryDao<History, Integer> historyDao) {
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

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

}
