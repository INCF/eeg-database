package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

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

    private List<ResearchGroup> researchGroupList;
    private List<Hardware> hardwareList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/hardware-definitions/list.html",method=RequestMethod.GET)
    public String showSelectForm(ModelMap model){
        log.debug("Processing hardware list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        fillAuthResearchGroupList();

        if(auth.isAdmin()){
            fillHardwareList(DEFAULT_ID);
            selectGroupCommand.setResearchGroup(DEFAULT_ID);
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                int myGroup = researchGroupList.get(0).getResearchGroupId();
                fillHardwareList(myGroup);
                selectGroupCommand.setResearchGroup(myGroup);
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
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model){
        fillAuthResearchGroupList();
        if(!researchGroupList.isEmpty()){
            fillHardwareList(selectGroupCommand.getResearchGroup());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("hardwareList", hardwareList);
        return "lists/hardware/list";
    }

    @RequestMapping(value="lists/hardware-definitions/delete.html")
    public String delete(@RequestParam("id") String idString) {
        log.debug("Deleting hardware.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (hardwareDao.canDelete(id)) {
                hardwareDao.delete(hardwareDao.read(id));
            } else {
                return "lists/itemUsed";
            }
        }

        return "lists/itemDeleted";
    }

    private void fillAuthResearchGroupList(){
        Person loggedUser = personDao.getLoggedPerson();
        ResearchGroup defaultGroup = new ResearchGroup(DEFAULT_ID,loggedUser,"Default Hardware","-");

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

}