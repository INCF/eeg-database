package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddHardwareCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;

public class AddHardwareController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<Hardware, Integer> hardwareDao;

  public AddHardwareController() {
    setCommandClass(AddHardwareCommand.class);
    setCommandName("addHardware");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ModelAndView mav = new ModelAndView(getSuccessView());

    log.debug("Processing form data.");
    AddHardwareCommand data = (AddHardwareCommand) command;

    if (!auth.userIsExperimenter()) {
      mav.setViewName("lists/userNotExperimenter");
    }

    log.debug("Creating new object");
    Hardware hardware = new Hardware();
    hardware.setTitle(data.getTitle());
    hardware.setType(data.getType());
    hardware.setDescription(data.getDescription());

    log.debug("Saving data to database");
    hardwareDao.create(hardware);

    log.debug("Returning MAV");
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
