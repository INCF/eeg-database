package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.MyAccountCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public class ChangePasswordController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;

  public ChangePasswordController() {
    setCommandClass(MyAccountCommand.class);
    setCommandName("myAccount");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    MyAccountCommand myAccountCommand = (MyAccountCommand) command;

    log.debug("Saving new password for actual user");
    String newPassword = myAccountCommand.getNewPassword();
    String passwordHash = ControllerUtils.getMD5String(newPassword);
    Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
    user.setPassword(passwordHash);
    log.debug("Setting password hash [" + passwordHash + "] for user [" + user.getUsername() + "]");
    personDao.update(user);

    log.debug("Returning MAV");
    ModelAndView mav = new ModelAndView(getSuccessView());
    return mav;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
