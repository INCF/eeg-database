package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@SessionAttributes("addDefectToPerson")
public class AddEyesDefectToPersonController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private PersonDao personDao;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    private AddEyesDefectToPersonValidator addEyesDefectToPersonValidator;

    @Autowired
	public AddEyesDefectToPersonController(AddEyesDefectToPersonValidator addEyesDefectToPersonValidator){
		this.addEyesDefectToPersonValidator = addEyesDefectToPersonValidator;
	}

    @RequestMapping(value="people/add-eyes-defect.html",method=RequestMethod.GET)
    protected String showForm( ModelMap model){
        log.debug("Preparing data for form");
        AddDefectToPersonCommand data = new AddDefectToPersonCommand();
        model.addAttribute("addDefectToPerson",data);

        return "people/addEyesDefectForm";

    }
    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addDefectToPerson") AddDefectToPersonCommand data, BindingResult result){
        addEyesDefectToPersonValidator.validate(data, result);
        if (result.hasErrors()) {
            return "people/addEyesDefectForm";
        }
        log.debug("Creating new object");
        VisualImpairment defect = visualImpairmentDao.read(data.getDefectId());
        Person person = personDao.read(data.getSubjectId());
        defect.getPersons().add(person);
        person.getVisualImpairments().add(defect);

        log.debug("Saving data to database");
        visualImpairmentDao.update(defect);

        String redirect =  "redirect:/people/detail.html?personId="+data.getSubjectId();
        return redirect;
    }
    @ModelAttribute("eyesDefectParams")
    private List<VisualImpairment> populateResearchGroupList(){
        log.debug("Loading parameter list for select box");
        List<VisualImpairment> list = new ArrayList<VisualImpairment>();
        Person loggedUser = personDao.getLoggedPerson();
         if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
               list.addAll(visualImpairmentDao.getAllRecords());
         }else{
            List<ResearchGroup> researchGroups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
            for(int i =0; i<researchGroups.size();i++){
            list.addAll(visualImpairmentDao.getRecordsByGroup(researchGroups.get(i).getResearchGroupId()));
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

    public VisualImpairmentDao getVisualImpairmentDao() {
        return visualImpairmentDao;
    }

    public void setVisualImpairmentDao(VisualImpairmentDao visualImpairmentDao) {
        this.visualImpairmentDao = visualImpairmentDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}

