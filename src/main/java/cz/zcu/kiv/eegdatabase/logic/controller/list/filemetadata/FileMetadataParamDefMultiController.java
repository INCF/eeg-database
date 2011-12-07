package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.list.SelectGroupCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author František Liška
 */
@Controller
@SessionAttributes("selectGroupCommand")
public class FileMetadataParamDefMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<FileMetadataParamDef> fileMetadataParamDefList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/file-metadata-definitions/list.html",method= RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing fileMetadataParamDef list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultFileMetadataParamDef = messageSource.getMessage("label.defaultFileMetadataParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultFileMetadataParamDef);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillFileMetadataParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillFileMetadataParamDefList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillFileMetadataParamDefList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillFileMetadataParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillFileMetadataParamDefList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("fileMetadataParamDefList", fileMetadataParamDefList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/fileMetadataParams/list";
    }

    @RequestMapping(value="lists/file-metadata-definitions/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultFileMetadataParamDef = messageSource.getMessage("label.defaultFileMetadataParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultFileMetadataParamDef);
        if(!researchGroupList.isEmpty()){
            fillFileMetadataParamDefList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("fileMetadataParamDefList", fileMetadataParamDefList);
        return "lists/fileMetadataParams/list";
    }

    @RequestMapping(value="lists/file-metadata-definitions/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting fileMetadataParamDef.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (fileMetadataParamDefDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default fileMetadataParamDef if it's from default group
                        if(!fileMetadataParamDefDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            fileMetadataParamDefDao.delete(fileMetadataParamDefDao.read(id));
                        }else{
                            return "lists/itemUsed";
                        }
                    }else{
                        FileMetadataParamDefGroupRel h = fileMetadataParamDefDao.getGroupRel(id,groupId);
                        if(!fileMetadataParamDefDao.isDefault(id)){ // delete only non default fileMetadataParamDef
                            fileMetadataParamDefDao.delete(fileMetadataParamDefDao.read(id));
                        }
                        fileMetadataParamDefDao.deleteGroupRel(h);
                    }
                }

            } else {
                return "lists/itemUsed";
            }
        }

        return "lists/itemDeleted";
    }

    private void fillAuthResearchGroupList(String defaultName){
        Person loggedUser = personDao.getLoggedPerson();

        ResearchGroup defaultGroup = new ResearchGroup(DEFAULT_ID,loggedUser,defaultName,"-");

        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
            researchGroupList = researchGroupDao.getAllRecords();
            researchGroupList.add(0,defaultGroup);
        }else{
            researchGroupList = researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        }
    }

    private void fillFileMetadataParamDefList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            fileMetadataParamDefList = fileMetadataParamDefDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                fileMetadataParamDefList = fileMetadataParamDefDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public FileMetadataParamDefDao getFileMetadataParamDefDao() {
        return fileMetadataParamDefDao;
    }

    public void setFileMetadataParamDefDao(FileMetadataParamDefDao fileMetadataParamDefDao) {
        this.fileMetadataParamDefDao = fileMetadataParamDefDao;
    }

    public HierarchicalMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

}
