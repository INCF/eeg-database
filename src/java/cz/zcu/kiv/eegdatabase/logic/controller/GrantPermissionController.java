package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 *
 * @author JiPER
 */
public class GrantPermissionController extends AbstractController {

  Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Running controller for granting permission");
    ModelAndView mav = new ModelAndView("system/permissionGranted");

    log.debug("Loading parameter from database");
    int personId = Integer.parseInt(request.getParameter("id"));

    log.debug("Updating person authority");
    Person person = personDao.read(personId);
    person.setAuthority("ROLE_EXPERIMENTER");

    log.debug("Saving changes to database");
    personDao.update(person);

    mav.addObject("person", person);

    log.debug("Returning MAV");
    return mav;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
