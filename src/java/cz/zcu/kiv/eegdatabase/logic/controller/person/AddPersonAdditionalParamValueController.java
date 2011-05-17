package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
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

public class AddPersonAdditionalParamValueController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<Person, Integer> personDao;
    private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;
    private GenericDao<PersonOptParamDef, Integer> personOptParamDefDao;

    public AddPersonAdditionalParamValueController() {
        setCommandClass(AddPersonAdditionalParamValueCommand.class);
        setCommandName("addPersonAdditionalParameter");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        log.debug("Preparing data for form");
        Map map = new HashMap<String, Object>();

        log.debug("Loading measuration info");
        int personId = Integer.parseInt(request.getParameter("personId"));
        Person person = personDao.read(personId);
        map.put("personDetail", person);

        log.debug("Loading parameter list for select box");
        List<PersonOptParamDef> list = personOptParamDefDao.getAllRecords();
        map.put("personAdditionalParams", list);

        log.debug("Returning map object");
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing form data.");
        AddPersonAdditionalParamValueCommand data = (AddPersonAdditionalParamValueCommand) command;

        log.debug("Creating new object");
        PersonOptParamVal val = new PersonOptParamVal();
        val.setId(new PersonOptParamValId(data.getPersonFormId(), data.getParamId()));
        val.setParamValue(data.getParamValue());

        log.debug("Saving object to database");
        personOptParamValDao.create(val);

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + data.getPersonFormId());
        return mav;
    }

    public GenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }

    public GenericDao<PersonOptParamDef, Integer> getPersonOptParamDefDao() {
        return personOptParamDefDao;
    }

    public void setPersonOptParamDefDao(GenericDao<PersonOptParamDef, Integer> personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }

    public GenericDao<Person, Integer> getPersonDao() {
        return personDao;
    }

    public void setPersonDao(GenericDao<Person, Integer> personDao) {
        this.personDao = personDao;
    }
}
