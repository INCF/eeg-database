package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.data.service.DataService;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Jindrich Pergler
 */
@Controller
@SessionAttributes("createGroup")
public class CreateGroupController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private DataService dataService;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private AuthorizationManager auth;

    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private WeatherDao weatherDao;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;

    private CreateGroupValidator createGroupValidator;

    @Autowired
    public CreateGroupController(CreateGroupValidator createGroupValidator){
        this.createGroupValidator = createGroupValidator;
    }

    @RequestMapping(value="groups/create-group.html",method= RequestMethod.GET)
    protected String showForm(ModelMap model) {
        CreateGroupCommand data = new CreateGroupCommand();
        model.addAttribute("createGroup",data);

        return "groups/createGroupForm";
    }

    @RequestMapping(value="groups/edit.html",method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("groupId") String idString,ModelMap model) {
        CreateGroupCommand data = new CreateGroupCommand();
        if (idString != null) {
            // Editing existing group
            int id = Integer.parseInt(idString);
            if (!auth.userIsAdminInGroup(id)) {
                return "redirect:/groups/detail.html?groupId=" + id;
            }
        }
        if (idString != null) {
            // Editing existing hardware
            int id = Integer.parseInt(idString);

            log.debug("Loading research group to the command object for editing.");
            ResearchGroup researchGroup = researchGroupDao.read(id);

            data.setId(researchGroup.getResearchGroupId());
            data.setResearchGroupTitle(researchGroup.getTitle());
            data.setResearchGroupDescription(researchGroup.getDescription());
        }
        model.addAttribute("createGroup",data);
        return "groups/createGroupForm";
    }

    @RequestMapping(method= RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("createGroup") CreateGroupCommand data, BindingResult result, ModelMap model) throws Exception {
        createGroupValidator.validate(data, result);

        log.debug("Processing form data with CreateGroupController.");
        ResearchGroup researchGroup;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing research group.");
            researchGroup = researchGroupDao.read(data.getId());
            researchGroup.setTitle(data.getResearchGroupTitle());
            researchGroup.setDescription(data.getResearchGroupDescription());
        } else {
            // Creating new
            log.debug("Creating new research group.");
            researchGroup = dataService.createResearchGroup(data.getResearchGroupTitle(),
                    data.getResearchGroupDescription(),
                    ControllerUtils.getLoggedUserName());
            addDefaultLists(researchGroup);
        }
        return "redirect:/groups/detail.html?groupId=" + researchGroup.getResearchGroupId();
    }

    private void addDefaultLists(ResearchGroup researchGroup){
        List<Hardware> hardwareList = hardwareDao.getDefaultRecords();
        for(int i = 0;i< hardwareList.size();i++){
            hardwareDao.createGroupRel(hardwareList.get(i),researchGroup);
        }
        List<Weather> weatherList = weatherDao.getDefaultRecords();
        for(int i = 0;i< weatherList.size();i++){
            weatherDao.createGroupRel(weatherList.get(i),researchGroup);
        }
        List<VisualImpairment> visualList = visualImpairmentDao.getDefaultRecords();
        for(int i = 0;i< visualList.size();i++){
            visualImpairmentDao.createGroupRel(visualList.get(i),researchGroup);
        }
        List<HearingImpairment> hearingList = hearingImpairmentDao.getDefaultRecords();
        for(int i = 0;i< hearingList.size();i++){
            hearingImpairmentDao.createGroupRel(hearingList.get(i),researchGroup);
        }
        List<PersonOptParamDef> personOptParamDefList = personOptParamDefDao.getDefaultRecords();
        for(int i = 0;i< personOptParamDefList.size();i++){
            personOptParamDefDao.createGroupRel(personOptParamDefList.get(i),researchGroup);
        }
        List<FileMetadataParamDef> fileMetadataParamDefList = fileMetadataParamDefDao.getDefaultRecords();
        for(int i = 0;i< fileMetadataParamDefList.size();i++){
            fileMetadataParamDefDao.createGroupRel(fileMetadataParamDefList.get(i),researchGroup);
        }
        List<ExperimentOptParamDef> experimentOptParamDefList = experimentOptParamDefDao.getDefaultRecords();
        for(int i = 0;i< experimentOptParamDefList.size();i++){
            experimentOptParamDefDao.createGroupRel(experimentOptParamDefList.get(i),researchGroup);
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}

