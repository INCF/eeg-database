package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRelId;
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
@SessionAttributes("addFileMetadataParamDef")
public class AddFileMetadataParamDefController{
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private final int DEFAULT_ID = -1;
    AddFileMetadataParamDefValidator addFileMetadataParamDefValidator;

    @Autowired
	public AddFileMetadataParamDefController(AddFileMetadataParamDefValidator addFileMetadataParamDefValidator){
		this.addFileMetadataParamDefValidator = addFileMetadataParamDefValidator;
	}

    @RequestMapping(value="lists/file-metadata-definitions/edit.html", method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String idString, @RequestParam("groupid") String idString2, ModelMap model, HttpServletRequest request){
        AddFileMetadataParamDefCommand data = new AddFileMetadataParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            model.addAttribute("userIsExperimenter", true);
            if (idString2 != null) {
                int id = Integer.parseInt(idString2);
                data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultFileMetadataParamDef = messageSource.getMessage("label.defaultFileMetadataParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultFileMetadataParamDef);
                 }
            }
            if (idString != null) {
                // Editation of existing fileMetadataParamDef
                int id = Integer.parseInt(idString);

                log.debug("Loading fileMetadataParamDef to the command object for editing.");
                FileMetadataParamDef fileMetadataParamDef = fileMetadataParamDefDao.read(id);
    
                data.setId(id);
                data.setParamName(fileMetadataParamDef.getParamName());
                data.setParamDataType(fileMetadataParamDef.getParamDataType());
            }
            model.addAttribute("addFileMetadataParamDef",data);

            return "lists/fileMetadataParams/addItemForm";
        }else{
           return "lists/userNotExperimenter";
        }
    }

    @RequestMapping(value="lists/file-metadata-definitions/add.html",method=RequestMethod.GET)
    protected String showAddForm(@RequestParam("groupid") String idString, ModelMap model, HttpServletRequest request) throws Exception {
        AddFileMetadataParamDefCommand data = new AddFileMetadataParamDefCommand();
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            if (idString != null) {
                int id = Integer.parseInt(idString);
                 data.setResearchGroupId(id);
                 if(id!=DEFAULT_ID){
                     String title = researchGroupDao.getResearchGroupTitle(id);
                     data.setResearchGroupTitle(title);
                 }else{
                     String defaultFileMetadataParamDef = messageSource.getMessage("label.defaultFileMetadataParamDef", null, RequestContextUtils.getLocale(request));
                     data.setResearchGroupTitle(defaultFileMetadataParamDef);
                 }
            }
            model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            model.addAttribute("addFileMetadataParamDef",data);
            return "lists/fileMetadataParams/addItemForm";
        }else{
            return "lists/userNotExperimenter";
        }
    }


    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addFileMetadataParamDef") AddFileMetadataParamDefCommand data, BindingResult result,ModelMap model) {
        log.debug("Processing form data.");
        if (auth.userIsExperimenter() || auth.isAdmin()) {
            addFileMetadataParamDefValidator.validate(data,result);
            if (result.hasErrors()) {
                return "lists/fileMetadataParams/addItemForm";
            }
            int fileMetadataParamDefId = data.getId();
            FileMetadataParamDef fileMetadataParamDef;
            if (fileMetadataParamDefId > 0) {
                // Editing
                log.debug("Editing existing fileMetadataParamDef object.");
                fileMetadataParamDef = fileMetadataParamDefDao.read(fileMetadataParamDefId);
                if(fileMetadataParamDefDao.isDefault(fileMetadataParamDefId)){
                     if(data.getResearchGroupId()!=DEFAULT_ID){
                         // new fileMetadataParamDef
                         FileMetadataParamDef newFileMetadataParamDef = new FileMetadataParamDef();
                         newFileMetadataParamDef.setDefaultNumber(0);
                         newFileMetadataParamDef.setParamName(data.getParamName());
                         newFileMetadataParamDef.setParamDataType(data.getParamDataType());
                         int newId = fileMetadataParamDefDao.create(newFileMetadataParamDef);
                         FileMetadataParamDefGroupRel rel = fileMetadataParamDefDao.getGroupRel(fileMetadataParamDefId,data.getResearchGroupId());
                         // delete old rel, create new one
                         FileMetadataParamDefGroupRelId newRelId = new FileMetadataParamDefGroupRelId();
                         FileMetadataParamDefGroupRel newRel = new FileMetadataParamDefGroupRel();
                         newRelId.setFileMetadataParamDefId(newId);
                         newRelId.setResearchGroupId(data.getResearchGroupId());
                         newRel.setId(newRelId);
                         newRel.setFileMetadataParamDef(newFileMetadataParamDef);
                         ResearchGroup r = researchGroupDao.read(data.getResearchGroupId());
                         newRel.setResearchGroup(r);
                         fileMetadataParamDefDao.deleteGroupRel(rel);
                         fileMetadataParamDefDao.createGroupRel(newRel);
                     }else{
                        if(!fileMetadataParamDefDao.hasGroupRel(fileMetadataParamDefId) && fileMetadataParamDefDao.canDelete(fileMetadataParamDefId)){
                            fileMetadataParamDef.setParamName(data.getParamName());
                            fileMetadataParamDef.setParamDataType(data.getParamDataType());
                        }else{
                            return "lists/itemUsed";
                        }
                     }
                }else{
                     fileMetadataParamDef.setParamName(data.getParamName());
                     fileMetadataParamDef.setParamDataType(data.getParamDataType());
                }
            } else {
                // Creating new
                fileMetadataParamDef = new FileMetadataParamDef();
                fileMetadataParamDef.setParamName(data.getParamName());
                fileMetadataParamDef.setParamDataType(data.getParamDataType());
                int pkGroup = data.getResearchGroupId();
                if(pkGroup == DEFAULT_ID){
                    log.debug("Creating new default fileMetadataParamDef object.");
                    fileMetadataParamDefDao.createDefaultRecord(fileMetadataParamDef);
                }else{
                    log.debug("Creating new group fileMetadataParamDef object.");
                    int pkFileMetadataParamDef = fileMetadataParamDefDao.create(fileMetadataParamDef);

                    FileMetadataParamDefGroupRelId fileMetadataParamDefGroupRelId = new FileMetadataParamDefGroupRelId(pkFileMetadataParamDef,pkGroup);
                    ResearchGroup researchGroup = researchGroupDao.read(pkGroup);
                    FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel = new FileMetadataParamDefGroupRel(fileMetadataParamDefGroupRelId,researchGroup,fileMetadataParamDef);
                    fileMetadataParamDefDao.createGroupRel(fileMetadataParamDefGroupRel);
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






































/*
package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddFileMetadataParamDefController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;

    public AddFileMetadataParamDefController() {
        setCommandClass(AddFileMetadataParamDefCommand.class);
        setCommandName("addFileMetadataParam");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddFileMetadataParamDefCommand data = (AddFileMetadataParamDefCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editing
            int id = Integer.parseInt(idString);

            log.debug("Loading file metadata parameter definition for editing.");
            FileMetadataParamDef definition = fileMetadataParamDefDao.read(id);

            data.setId(id);
            data.setParamDataType(definition.getParamDataType());
            data.setParamName(definition.getParamName());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data.");
        AddFileMetadataParamDefCommand data = (AddFileMetadataParamDefCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        FileMetadataParamDef param;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing file metadata parameter definition.");
            param = fileMetadataParamDefDao.read(data.getId());
            param.setParamName(data.getParamName());
            param.setParamDataType(data.getParamDataType());
            fileMetadataParamDefDao.update(param);
        } else {
            // Creating new
            log.debug("Creating new object");
            param = new FileMetadataParamDef();
            param.setParamName(data.getParamName());
            param.setParamDataType(data.getParamDataType());
            fileMetadataParamDefDao.create(param);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
*/
