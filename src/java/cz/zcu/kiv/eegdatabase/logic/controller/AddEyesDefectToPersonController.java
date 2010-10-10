package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddDefectToPersonCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public class AddEyesDefectToPersonController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<Person, Integer> personDao;
    private GenericDao<VisualImpairment, Integer> eyesDefectDao;

    public AddEyesDefectToPersonController() {
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
        List<VisualImpairment> list = eyesDefectDao.getAllRecords();
        map.put("eyesDefectParams", list);

        log.debug("Returning map object");
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing form data.");
        AddDefectToPersonCommand data = (AddDefectToPersonCommand) command;

        log.debug("Creating new object");
        VisualImpairment defect = eyesDefectDao.read(data.getDefectId());
        Person person = personDao.read(data.getSubjectId());
        defect.getPersons().add(person);
        person.getVisualImpairments().add(defect);

        log.debug("Saving data to database");
        eyesDefectDao.update(defect);

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + data.getSubjectId());
        return mav;
    }

    public GenericDao<VisualImpairment, Integer> getEyesDefectDao() {
        return eyesDefectDao;
    }

    public void setEyesDefectDao(GenericDao<VisualImpairment, Integer> eyesDefectDao) {
        this.eyesDefectDao = eyesDefectDao;
    }

    public GenericDao<Person, Integer> getPersonDao() {
        return personDao;
    }

    public void setPersonDao(GenericDao<Person, Integer> personDao) {
        this.personDao = personDao;
    }
}
