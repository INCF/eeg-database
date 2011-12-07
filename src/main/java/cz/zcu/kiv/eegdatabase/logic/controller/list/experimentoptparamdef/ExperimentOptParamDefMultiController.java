package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;
import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
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
public class ExperimentOptParamDefMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<ExperimentOptParamDef> experimentOptParamDefList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/experiment-optional-parameters/list.html",method= RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing experimentOptParamDef list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultExperimentOptParamDef = messageSource.getMessage("label.defaultExperimentOptParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultExperimentOptParamDef);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillExperimentOptParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillExperimentOptParamDefList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillExperimentOptParamDefList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillExperimentOptParamDefList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillExperimentOptParamDefList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("experimentOptParamDefList", experimentOptParamDefList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/measurationAdditionalParams/list";
    }

    @RequestMapping(value="lists/experiment-optional-parameters/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultExperimentOptParamDef = messageSource.getMessage("label.defaultExperimentOptParamDef", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultExperimentOptParamDef);
        if(!researchGroupList.isEmpty()){
            fillExperimentOptParamDefList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("experimentOptParamDefList", experimentOptParamDefList);
        return "lists/measurationAdditionalParams/list";
    }

    @RequestMapping(value="lists/experiment-optional-parameters/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting experimentOptParamDef.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (experimentOptParamDefDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default experimentOptParamDef if it's from default group
                        if(!experimentOptParamDefDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            experimentOptParamDefDao.delete(experimentOptParamDefDao.read(id));
                        }else{
                            return "lists/itemUsed";
                        }
                    }else{
                        ExperimentOptParamDefGroupRel h = experimentOptParamDefDao.getGroupRel(id,groupId);
                        if(!experimentOptParamDefDao.isDefault(id)){ // delete only non default experimentOptParamDef
                            experimentOptParamDefDao.delete(experimentOptParamDefDao.read(id));
                        }
                        experimentOptParamDefDao.deleteGroupRel(h);
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

    private void fillExperimentOptParamDefList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            experimentOptParamDefList = experimentOptParamDefDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                experimentOptParamDefList = experimentOptParamDefDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public ExperimentOptParamDefDao getExperimentOptParamDefDao() {
        return experimentOptParamDefDao;
    }

    public void setExperimentOptParamDefDao(ExperimentOptParamDefDao experimentOptParamDefDao) {
        this.experimentOptParamDefDao = experimentOptParamDefDao;
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






























/**
import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JiPER
 *//*
public class ExperimentOptParamDefMultiController extends MultiActionController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Processing measuration additional parameters list controller");
        ModelAndView mav = new ModelAndView("lists/measurationAdditionalParams/list");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        List<ExperimentOptParamDef> list = experimentOptParamDefDao.getItemsForList();
        mav.addObject("measurationAdditionalParamsList", list);
        return mav;
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Deleting experiment  optional parameter.");
        ModelAndView mav = new ModelAndView("lists/itemDeleted");

        String idString = request.getParameter("id");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (experimentOptParamDefDao.canDelete(id)) {
                experimentOptParamDefDao.delete(experimentOptParamDefDao.read(id));
            } else {
                mav.setViewName("lists/itemUsed");
            }
        }

        return mav;
    }
}
   */