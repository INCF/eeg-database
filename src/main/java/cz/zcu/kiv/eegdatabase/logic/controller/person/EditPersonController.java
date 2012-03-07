package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

public class EditPersonController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private PersonDao personDao;
    private GenericDao<EducationLevel, Integer> educationLevelDao;
    @Autowired
    private AuthorizationManager auth;


    public EditPersonController() {
        setCommandClass(AddPersonCommand.class);
        setCommandName("addPerson");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddPersonCommand data = (AddPersonCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            log.debug("Loading person to command object for editing.");
            Person person = personDao.read(id);

            data.setId(id);
            data.setGivenname(person.getGivenname());
            data.setSurname(person.getSurname());
            data.setDateOfBirth(ControllerUtils.getDateFormat().format(person.getDateOfBirth()));
            data.setGender(new Character(person.getGender()).toString());
            data.setEmail(person.getEmail());
            data.setPhoneNumber(person.getPhoneNumber());
            data.setNote(person.getNote());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);
            if (!auth.userCanViewPersonDetails(id)) {
                mav.setViewName("redirect:/people/list.html");
            }
        }

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        AddPersonCommand data = (AddPersonCommand) command;

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);
            if (!auth.userCanViewPersonDetails(id)) {
                mav.setViewName("redirect:/people/list.html");
                return mav;
            }

            log.debug("Loading existing person from database.");
            Person person = personDao.read(id);

            log.debug("Setting givenname = " + data.getGivenname());
            person.setGivenname(data.getGivenname());

            log.debug("Setting surname = " + data.getSurname());
            person.setSurname(data.getSurname());

            Date dateOfBirth = ControllerUtils.getDateFormat().parse(data.getDateOfBirth());
            person.setDateOfBirth(new Timestamp(dateOfBirth.getTime()));
            log.debug("Setting date of birth = " + dateOfBirth);

            log.debug("Setting gender = " + data.getGender());
            person.setGender(data.getGender().charAt(0));

            log.debug("Setting email = " + data.getEmail());
            person.setEmail(data.getEmail());

            log.debug("Setting phone number = " + data.getPhoneNumber());
            person.setPhoneNumber(data.getPhoneNumber());

            log.debug("Setting note = " + data.getNote());
            person.setNote(data.getNote());
            person.setLaterality(data.getLaterality().charAt(0));
            person.setEducationLevel(educationLevelDao.read(data.getEducationLevel()));

            log.debug("Creating new Person object");
            personDao.update(person);

            mav.setViewName(getSuccessView() + person.getPersonId());
        }

        log.debug("Returning MAV");
        return mav;
    }

    public GenericDao<EducationLevel, Integer> getEducationLevelDao() {
        return educationLevelDao;
    }

    public void setEducationLevelDao(GenericDao<EducationLevel, Integer> educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }
}
