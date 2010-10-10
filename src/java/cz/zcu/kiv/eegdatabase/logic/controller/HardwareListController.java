package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
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
public class HardwareListController extends AbstractController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<Hardware, Integer> hardwareDao;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Processing hardware list controller");
    ModelAndView mav = new ModelAndView("lists/hardware/list");

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());

    List<Hardware> list = hardwareDao.getAllRecords();
    mav.addObject("hardwareList", list);
    return mav;
  }

  public GenericDao<Hardware, Integer> getHardwareDao() {
    return hardwareDao;
  }

  public void setHardwareDao(GenericDao<Hardware, Integer> hardwareDao) {
    this.hardwareDao = hardwareDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
