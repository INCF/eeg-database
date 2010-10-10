package cz.zcu.kiv.eegdatabase.logic.controller.admin;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ChangeUserRoleCommand;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controller for changing the global role of user.
 *
 * @author Jindra
 */
public class ChangeUserRole extends SimpleFormController {

  private PersonDao personDao;

  public ChangeUserRole() {
    setCommandClass(ChangeUserRoleCommand.class);
    setCommandName("changeUserRole");
  }

  @Override
  protected ModelAndView onSubmit(Object command) throws Exception {
    ChangeUserRoleCommand data = (ChangeUserRoleCommand) command;

    Person person = personDao.getPerson(data.getUserName());
    person.setAuthority(data.getUserRole());
    personDao.update(person);

    return new ModelAndView(getSuccessView());
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
