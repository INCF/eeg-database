package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairmentGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairmentGroupRelId;
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
@SessionAttributes("addVisualImpairment")
public class AddVisualImpairmentController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddVisualImpairmentValidator addVisualImpairmentValidator;

    @Autowired
	public AddVisualImpairmentController(AddVisualImpairmentValidator addVisualImpairmentValidator){
		this.addVisualImpairmentValidator = addVisualImpairmentValidator;
	}

    @RequestMapping(value="lists/visual-impairments/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddVisualImpairmentCommand data = new AddVisualImpairmentCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultVisualImpairment = messageSource.getMessage("label.defaultVisualImpairments", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultVisualImpairment);
                 }
            }
            if (idString != null) {
                // Editation of existing visualImpairment
                int id = Integer.parseInt(idString);

                log.debug("Loading visualImpairment to the command object for editing.");
                VisualImpairment visualImpairment = visualImpairmentDao.read(id);
    
                data.setVisualImpairmentId(id);
                data.setDescription(visualImpairment.getDescription());
            }
            model.addAttribute("addVisualImpairment",data);

            return "lists/eyesDefect/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/visual-impairments/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddVisualImpairmentCommand data = new AddVisualImpairmentCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultVisualImpairment = messageSource.getMessage("label.defaultVisualImpairments", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultVisualImpairment);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addVisualImpairment",data);
            return "lists/eyesDefect/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addVisualImpairment") AddVisualImpairmentCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");

        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addVisualImpairmentValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/eyesDefect/addItemForm";
            }
            int visualImpairmentId = data.getVisualImpairmentId();
            VisualImpairment visualImpairment;
            if (visualImpairmentId > 0) {
                // Editing
                log.debug("Editing existing visual impairment object.");
                visualImpairment = visualImpairmentDao.read(visualImpairmentId);

                if(visualImpairmentDao.isDefault(visualImpairmentId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new visualImpairment
                         VisualImpairment newVisualImpairment = new VisualImpairment();
                         newVisualImpairment.setDefaultNumber(0);
                         newVisualImpairment.setDescription(data.getDescription());
                         int newId = visualImpairmentDao.create(newVisualImpairment);
                         VisualImpairmentGroupRel rel = visualImpairmentDao.getGroupRel(visualImpairmentId,data.getResearchGroupId());
                         // delete old rel, create new one
                         VisualImpairmentGroupRelId newRelId = new VisualImpairmentGroupRelId();
                         VisualImpairmentGroupRel newRel = new VisualImpairmentGroupRel();
                         newRelId.setVisualImpairmentId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setVisualImpairment(newVisualImpairment);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         visualImpairmentDao.deleteGroupRel(rel);
                         visualImpairmentDao.createGroupRel(newRel);
                     }else{
                        if(!visualImpairmentDao.hasGroupRel(visualImpairmentId) && visualImpairmentDao.canDelete(visualImpairmentId)){;
                            visualImpairment.setDescription(data.getDescription());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     visualImpairment.setDescription(data.getDescription());
                }
            } else {
                // Creating new
                visualImpairment = new VisualImpairment();
                visualImpairment.setDescription(data.getDescription());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default visual impairment object.");
                    visualImpairmentDao.createDefaultRecord(visualImpairment);
                }else{
                    log.debug("Creating new group visual impairment object.");
                    int pkVisualImpairment = visualImpairmentDao.create(visualImpairment);

                    VisualImpairmentGroupRelId visualImpairmentGroupRelId = new VisualImpairmentGroupRelId(pkVisualImpairment,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    VisualImpairmentGroupRel visualImpairmentGroupRel = new VisualImpairmentGroupRel(visualImpairmentGroupRelId,researchGroup,visualImpairment);
                    visualImpairmentDao.createGroupRel(visualImpairmentGroupRel);
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
