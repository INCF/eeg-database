package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.aspectj.apache.bcel.generic.Select;
import org.aspectj.org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author František Liška
 */
@Controller
@RequestMapping("lists/hardware-definitions/list.html")
@SessionAttributes("selectGroupCommand")
public class SelectGroupController{
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private PersonDao personDao;
    private Person loggedUser;
    private List<ResearchGroup> researchGroupList;
    private List<Hardware> hardwareList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(method=RequestMethod.GET)
    public String showSelectForm(ModelMap model){
        loggedUser =  personDao.getLoggedPerson();
        fillAuthResearchGroupList();
        if(loggedUser.getAuthority().equals("ROLE_ADMIN")){
            fillHardwareList(DEFAULT_ID);
        }else{
            if(!researchGroupList.isEmpty()){
                int myGroup = researchGroupList.get(0).getResearchGroupId();
                fillHardwareList(myGroup);
            }
        }

        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("hardwareList", hardwareList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/hardware/list";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model){
        fillAuthResearchGroupList();
        fillHardwareList(selectGroupCommand.getResearchGroup());

        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("hardwareList", hardwareList);
        return "lists/hardware/list";
    }

    private void fillAuthResearchGroupList(){
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