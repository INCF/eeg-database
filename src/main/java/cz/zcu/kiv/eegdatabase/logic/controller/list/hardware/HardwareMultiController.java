package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.list.SelectGroupCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author František Liška
 */
@Controller
@SessionAttributes("selectGroupCommand")
public class HardwareMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<Hardware> hardwareList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/hardware-definitions/list.html",method=RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing hardware list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultHardware = messageSource.getMessage("label.defaultHardware", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultHardware);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillHardwareList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillHardwareList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillHardwareList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillHardwareList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillHardwareList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("hardwareList", hardwareList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/hardware/list";
    }

    @RequestMapping(value="lists/hardware-definitions/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultHardware = messageSource.getMessage("label.defaultHardware", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultHardware);
        if(!researchGroupList.isEmpty()){
            fillHardwareList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("hardwareList", hardwareList);
        return "lists/hardware/list";
    }

    @RequestMapping(value="lists/hardware-definitions/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting hardware.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (hardwareDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default hardware if it's from default group
                        if(!hardwareDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            hardwareDao.delete(hardwareDao.read(id));
                        }else{
                            System.out.println("hasGroupRel");
                            return "lists/itemUsed";
                        }
                    }else{
                        HardwareGroupRel h = hardwareDao.getGroupRel(id,groupId);
                        if(!hardwareDao.isDefault(id)){ // delete only non default hardware
                            hardwareDao.delete(hardwareDao.read(id));
                        }
                        hardwareDao.deleteGroupRel(h);
                    }
                }

            } else {
                System.out.println("cannotDelete");
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

    private void fillHardwareList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            hardwareList = hardwareDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                hardwareList = hardwareDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public HardwareDao getHardwareDao() {
        return hardwareDao;
    }

    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
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