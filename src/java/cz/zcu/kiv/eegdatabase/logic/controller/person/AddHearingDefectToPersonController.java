package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddHearingDefectToPersonController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<Person, Integer> personDao;
    private GenericDao<HearingImpairment, Integer> hearingImpairmentDao;

    public AddHearingDefectToPersonController() {
        setCommandClass(AddDefectToPersonCommand.class);
        setCommandName("addDefectToPerson");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        log.debug("Preparing data for form");
        Map map = new HashMap<String, Object>();

        log.debug("Loading person info");
        int personId = Integer.parseInt(request.getParameter("personId"));
        Person person = personDao.read(personId);
        map.put("personDetail", person);

        log.debug("Loading parameter list for select box");
        List<HearingImpairment> list = hearingImpairmentDao.getAllRecords();
        map.put("hearingDefectParams", list);

        log.debug("Returning map object");
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing form data.");
        AddDefectToPersonCommand data = (AddDefectToPersonCommand) command;

        log.debug("Creating new object");
        HearingImpairment defect = hearingImpairmentDao.read(data.getDefectId());
        Person person = personDao.read(data.getSubjectId());
        defect.getPersons().add(person);
        person.getHearingImpairments().add(defect);

        log.debug("Saving data to database");
        hearingImpairmentDao.update(defect);

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + data.getSubjectId());
        return mav;
    }

    public GenericDao<HearingImpairment, Integer> getHearingImpairmentDao() {
        return hearingImpairmentDao;
    }

    public void setHearingImpairmentDao(GenericDao<HearingImpairment, Integer> hearingImpairmentDao) {
        this.hearingImpairmentDao = hearingImpairmentDao;
    }

    public GenericDao<Person, Integer> getPersonDao() {
        return personDao;
    }

    public void setPersonDao(GenericDao<Person, Integer> personDao) {
        this.personDao = personDao;
    }
}
