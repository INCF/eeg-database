package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
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
public class MeasurationAdditionalParamListController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Processing measuration additional parameters list controller");
    ModelAndView mav = new ModelAndView("lists/measurationAdditionalParams/list");

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());
    
    
    List<ExperimentOptParamDef> list = experimentOptParamDefDao.getAllRecords();
    mav.addObject("measurationAdditionalParamsList", list);
    return mav;
  }

  public GenericDao<ExperimentOptParamDef, Integer> getExperimentOptParamDefDao() {
    return experimentOptParamDefDao;
  }

  public void setExperimentOptParamDefDao(GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao) {
    this.experimentOptParamDefDao = experimentOptParamDefDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
