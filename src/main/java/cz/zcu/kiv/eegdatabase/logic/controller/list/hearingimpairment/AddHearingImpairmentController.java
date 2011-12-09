package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairmentGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairmentGroupRelId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("addHearingImpairment")
public class AddHearingImpairmentController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddHearingImpairmentValidator addHearingImpairmentValidator;

    @Autowired
	public AddHearingImpairmentController(AddHearingImpairmentValidator addHearingImpairmentValidator){
		this.addHearingImpairmentValidator = addHearingImpairmentValidator;
	}

    @RequestMapping(value="lists/hearing-impairments/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddHearingImpairmentCommand data = new AddHearingImpairmentCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultHearingImpairment = messageSource.getMessage("label.defaultHearingImpairments", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultHearingImpairment);
                 }
            }
            if (idString != null) {
                // Editation of existing hearingImpairment
                int id = Integer.parseInt(idString);

                log.debug("Loading hearingImpairment to the command object for editing.");
                HearingImpairment hearingImpairment = hearingImpairmentDao.read(id);
    
                data.setHearingImpairmentId(id);
                data.setDescription(hearingImpairment.getDescription());
            }
            model.addAttribute("addHearingImpairment",data);

            return "lists/hearingDefect/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/hearing-impairments/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddHearingImpairmentCommand data = new AddHearingImpairmentCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultHearingImpairment = messageSource.getMessage("label.defaultHearingImpairments", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultHearingImpairment);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addHearingImpairment",data);
            return "lists/hearingDefect/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addHearingImpairment") AddHearingImpairmentCommand data, BindingResult result) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addHearingImpairmentValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/hearingDefect/addItemForm";
            }
            int hearingImpairmentId = data.getHearingImpairmentId();
            HearingImpairment hearingImpairment;
            if (hearingImpairmentId > 0) {
                // Editing
                log.debug("Editing existing hearing impairment object.");
                hearingImpairment = hearingImpairmentDao.read(hearingImpairmentId);

                if(hearingImpairmentDao.isDefault(hearingImpairmentId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new hearingImpairment
                         HearingImpairment newHearingImpairment = new HearingImpairment();
                         newHearingImpairment.setDefaultNumber(0);
                         newHearingImpairment.setDescription(data.getDescription());
                         int newId = hearingImpairmentDao.create(newHearingImpairment);
                         HearingImpairmentGroupRel rel = hearingImpairmentDao.getGroupRel(hearingImpairmentId,data.getResearchGroupId());
                         // delete old rel, create new one
                         HearingImpairmentGroupRelId newRelId = new HearingImpairmentGroupRelId();
                         HearingImpairmentGroupRel newRel = new HearingImpairmentGroupRel();
                         newRelId.setHearingImpairmentId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setHearingImpairment(newHearingImpairment);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         hearingImpairmentDao.deleteGroupRel(rel);
                         hearingImpairmentDao.createGroupRel(newRel);
                     }else{
                        if(!hearingImpairmentDao.hasGroupRel(hearingImpairmentId) && hearingImpairmentDao.canDelete(hearingImpairmentId)){;
                            hearingImpairment.setDescription(data.getDescription());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     hearingImpairment.setDescription(data.getDescription());
                }
            } else {
                // Creating new
                hearingImpairment = new HearingImpairment();
                hearingImpairment.setDescription(data.getDescription());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default hearing impairment object.");
                    hearingImpairmentDao.createDefaultRecord(hearingImpairment);
                }else{
                    log.debug("Creating new group hearing impairment object.");
                    int pkHearingImpairment = hearingImpairmentDao.create(hearingImpairment);

                    HearingImpairmentGroupRelId hearingImpairmentGroupRelId = new HearingImpairmentGroupRelId(pkHearingImpairment,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    HearingImpairmentGroupRel hearingImpairmentGroupRel = new HearingImpairmentGroupRel(hearingImpairmentGroupRelId,researchGroup,hearingImpairment);
                    hearingImpairmentDao.createGroupRel(hearingImpairmentGroupRel);
                }

            }
            log.debug("Returning MAV");
            String redirect = "redirect:list.html?groupid="+data.getResearchGroupId();
            return redirect;
        }else{
            return "lists/userNotExperimenter";
        }
    }
}
