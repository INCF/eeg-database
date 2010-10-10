package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class ConfirmController extends AbstractController {
  private Log log = LogFactory.getLog(getClass());
 
  private PersonDao personDao;



  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("Processing ConfirmController controller");
    ModelAndView mav; 
 
    Person person = personDao.getPersonByHash(request.getParameter("activation"));

    if (person==null){
      return new ModelAndView("system/registrationFalse");
    }

    if (person.isConfirmed()) {
      return new ModelAndView("system/registrationConfirmRepeated");
    }
    if (confirmedInTime(System.currentTimeMillis(), person)){
       person.setConfirmed(true);
       mav = new ModelAndView("system/confirmationSuccessfull");
      }
    else{
      mav = new ModelAndView("system/registrationFalse");
      personDao.delete(person);
    }
    
    return mav;
}

  public boolean confirmedInTime(long clickTime, Person person){
    long requestTime = person.getRegistrationDate().getTime();
    //8 days in ms
    long maximumDelay = 691200000;  
    return (clickTime - requestTime < maximumDelay);
  }



   public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
