package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
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
public class AddHearingDefectToPersonController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    private AddHearingDefectToPersonValidator addHearingDefectToPersonValidator;

    @Autowired
	public AddHearingDefectToPersonController(AddHearingDefectToPersonValidator addHearingDefectToPersonValidator){
		this.addHearingDefectToPersonValidator = addHearingDefectToPersonValidator;
	}

    @RequestMapping(value="people/add-hearing-defect.html",method=RequestMethod.GET)
    protected String showForm( ModelMap model){
        log.debug("Preparing data for form");
        AddDefectToPersonCommand data = new AddDefectToPersonCommand();
        model.addAttribute("addDefectToPerson",data);

        return "people/addHearingDefectForm";

    }
    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addDefectToPerson") AddDefectToPersonCommand data, BindingResult result){
        addHearingDefectToPersonValidator.validate(data, result);
        if (result.hasErrors()) {
            return "people/addHearingDefectForm";
        }
        log.debug("Creating new object");
        HearingImpairment defect = hearingImpairmentDao.read(data.getDefectId());
        Person person = personDao.read(data.getSubjectId());
        defect.getPersons().add(person);
        person.getHearingImpairments().add(defect);

        log.debug("Saving data to database");
        hearingImpairmentDao.update(defect);

        String redirect =  "redirect:/people/detail.html?personId="+data.getSubjectId();
        return redirect;
    }
    @ModelAttribute("hearingDefectParams")
    private List<HearingImpairment> populateResearchGroupList(){
        log.debug("Loading parameter list for select box");
        List<HearingImpairment> list = new ArrayList<HearingImpairment>();
        Person loggedUser = personDao.getLoggedPerson();
         if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
               list.addAll(hearingImpairmentDao.getAllRecords());
         }else{
            List<ResearchGroup> researchGroups = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
            for(int i =0; i<researchGroups.size();i++){
            list.addAll(hearingImpairmentDao.getRecordsByGroup(researchGroups.get(i).getResearchGroupId()));
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

    public HearingImpairmentDao getHearingImpairmentDao() {
        return hearingImpairmentDao;
    }

    public void setHearingImpairmentDao(HearingImpairmentDao hearingImpairmentDao) {
        this.hearingImpairmentDao = hearingImpairmentDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
