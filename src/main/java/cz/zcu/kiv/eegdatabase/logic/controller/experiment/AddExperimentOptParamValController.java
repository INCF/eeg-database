package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author František Liška
 */
@Controller
@SessionAttributes("addMeasurationAdditionalParameter")
public class AddExperimentOptParamValController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private ExperimentDao experimentDao;
    @Autowired
    @Qualifier("experimentOptParamValDao")
    private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;

    private AddExperimentOptParamValValidator addExperimentOptParamValValidator;

    @Autowired
    public AddExperimentOptParamValController(AddExperimentOptParamValValidator addExperimentOptParamValValidator){
        this.addExperimentOptParamValValidator = addExperimentOptParamValValidator;
    }


    @RequestMapping(value="experiments/add-optional-parameter.html",method= RequestMethod.GET)
    protected String showForm(ModelMap model){
        log.debug("Preparing data for form");
        AddExperimentOptParamValCommand data = new AddExperimentOptParamValCommand();
        model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
        model.addAttribute("addMeasurationAdditionalParameter", data);
        return "experiments/optionalParams/addItemForm";

    }

    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addMeasurationAdditionalParameter") AddExperimentOptParamValCommand data, BindingResult result){
        addExperimentOptParamValValidator.validate(data, result);
        if (result.hasErrors()) {
            return "experiments/optionalParams/addItemForm";
        }

        log.debug("Processing form data.");

        log.debug("Creating new object");
        ExperimentOptParamVal val = new ExperimentOptParamVal();
        val.setId(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
        val.setParamValue(data.getParamValue());

        log.debug("Saving object to database");
        experimentOptParamValDao.create(val);

       String redirect =  "redirect:/experiments/detail.html?experimentId="+data.getMeasurationFormId();
        return redirect;
    }

    @ModelAttribute("measurationAdditionalParams")
    protected List<ExperimentOptParamDef> populateOptParamList(@RequestParam("experimentId") String idString){
       log.debug("Loading parameter list for select box");
        List<ExperimentOptParamDef> list = new ArrayList<ExperimentOptParamDef>();
        Person loggedUser = personDao.getLoggedPerson();
        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
           list.addAll(experimentOptParamDefDao.getAllRecords());
        }else{
            int experimentId = 0;
            if(idString!=null){
                experimentId = Integer.parseInt(idString) ;
            }
            Experiment e = (Experiment)experimentDao.read(experimentId);
            int groupId = e.getResearchGroup().getResearchGroupId();
            return experimentOptParamDefDao.getRecordsByGroup(groupId);
        }

       return list;
    }

    @ModelAttribute("measurationDetail")
    private Experiment populateExperimentDetail(@RequestParam("experimentId") String idString){
        log.debug("Loading experiment info");
        int experimentId = Integer.parseInt(idString);
        Experiment experiment = (Experiment) experimentDao.read(experimentId);
        return experiment;
    }

    @ModelAttribute("researchGroupTitle")
    private String fillResearchGroupTitleForExperiment(@RequestParam("experimentId") String idString){
        log.debug("Loading experiment info");
        int experimentId = Integer.parseInt(idString);
        Experiment experiment = (Experiment) experimentDao.read(experimentId);
        return experiment.getResearchGroup().getTitle();
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

    public GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> getExperimentOptParamValDao() {
        return experimentOptParamValDao;
    }

    public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
        this.experimentOptParamValDao = experimentOptParamValDao;
    }

    public ExperimentOptParamDefDao getExperimentOptParamDefDao() {
        return experimentOptParamDefDao;
    }

    public void setExperimentOptParamDefDao(ExperimentOptParamDefDao experimentOptParamDefDao) {
        this.experimentOptParamDefDao = experimentOptParamDefDao;
    }

    public AddExperimentOptParamValValidator getAddExperimentOptParamValValidator() {
        return addExperimentOptParamValValidator;
    }

    public void setAddExperimentOptParamValValidator(AddExperimentOptParamValValidator addExperimentOptParamValValidator) {
        this.addExperimentOptParamValValidator = addExperimentOptParamValValidator;
    }
}

