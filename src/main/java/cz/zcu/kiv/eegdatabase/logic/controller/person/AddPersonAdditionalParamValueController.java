package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes("addPersonAdditionalParameter")
public class AddPersonAdditionalParamValueController{

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    @Qualifier("personOptParamValDao")
    private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;
    private AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator;

    @Autowired
    public AddPersonAdditionalParamValueController(AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator){
        this.addPersonAdditionalParamValueValidator = addPersonAdditionalParamValueValidator;
    }


    @RequestMapping(value="people/add-optional-parameter.html",method= RequestMethod.GET)
    protected String showForm(ModelMap model){
        log.debug("Preparing data for form");
        AddPersonAdditionalParamValueCommand data = new AddPersonAdditionalParamValueCommand();
        model.addAttribute("addPersonAdditionalParameter", data);
        return "people/optionalParams/addItemForm";

    }

    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addPersonAdditionalParameter") AddPersonAdditionalParamValueCommand data, BindingResult result){
        addPersonAdditionalParamValueValidator.validate(data, result);
        if (result.hasErrors()) {
            return "people/optionalParams/addItemForm";
        }

        log.debug("Processing form data.");

        log.debug("Creating new object");
        PersonOptParamVal val = new PersonOptParamVal();
        val.setId(new PersonOptParamValId(data.getPersonFormId(), data.getParamId()));
        val.setParamValue(data.getParamValue());

        log.debug("Saving object to database");
        personOptParamValDao.create(val);

       String redirect =  "redirect:/people/detail.html?personId="+data.getPersonFormId();
        return redirect;
    }

    @ModelAttribute("personAdditionalParams")
    protected List<PersonOptParamDef> populateOptParamList(){
       log.debug("Loading parameter list for select box");
        List<PersonOptParamDef> list = new ArrayList<PersonOptParamDef>();
        Person loggedUser = personDao.getLoggedPerson();
        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
           list.addAll(personOptParamDefDao.getAllRecords());
        }else{
            List<ResearchGroup> researchGroups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
            for(int i =0; i<researchGroups.size();i++){
                list.addAll(personOptParamDefDao.getRecordsByGroup(researchGroups.get(i).getResearchGroupId()));
            }
        }

       return list;
    }

    @ModelAttribute("personDetail")
    private Person populatePersonDetail(@RequestParam("personId") String idString){
        log.debug("Loading person info");
        int personId = Integer.parseInt(idString);
        Person person = personDao.read(personId);
        return person;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public GenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }

    public PersonOptParamDefDao getPersonOptParamDefDao() {
        return personOptParamDefDao;
    }

    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }

    public AddPersonAdditionalParamValueValidator getAddPersonAdditionalParamValueValidator() {
        return addPersonAdditionalParamValueValidator;
    }

    public void setAddPersonAdditionalParamValueValidator(AddPersonAdditionalParamValueValidator addPersonAdditionalParamValueValidator) {
        this.addPersonAdditionalParamValueValidator = addPersonAdditionalParamValueValidator;
    }
}
