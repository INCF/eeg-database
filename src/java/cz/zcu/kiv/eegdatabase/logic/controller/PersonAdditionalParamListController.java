package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author JiPER
 */
public class PersonAdditionalParamListController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<PersonOptParamDef, Integer> personOptParamDefDao;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Processing person additional parameters list controller");
    ModelAndView mav = new ModelAndView("lists/personAdditionalParams/list");

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());

    List<PersonOptParamDef> list = personOptParamDefDao.getAllRecords();
    mav.addObject("personAdditionalParamsList", list);
    return mav;
  }

  public GenericDao<PersonOptParamDef, Integer> getPersonOptParamDefDao() {
    return personOptParamDefDao;
  }

  public void setPersonOptParamDefDao(GenericDao<PersonOptParamDef, Integer> personOptParamDefDao) {
    this.personOptParamDefDao = personOptParamDefDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
