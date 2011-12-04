package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairmentGroupRel;
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
public class HearingImpairmentMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<HearingImpairment> hearingImpairmentList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/hearing-impairments/list.html",method=RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing hearing defect list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultHearingImpairment = messageSource.getMessage("label.defaultHearingImpairments", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultHearingImpairment);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillHearingImpairmentList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillHearingImpairmentList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillHearingImpairmentList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillHearingImpairmentList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillHearingImpairmentList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("hearingImpairmentList", hearingImpairmentList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/hearingDefect/list";
    }

    @RequestMapping(value="lists/hearing-impairments/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultHearingImpairment = messageSource.getMessage("label.defaultHearingImpairments", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultHearingImpairment);
        if(!researchGroupList.isEmpty()){
            fillHearingImpairmentList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("hearingImpairmentList", hearingImpairmentList);
        return "lists/hearingDefect/list";
    }

    @RequestMapping(value="lists/hearing-impairments/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting hearing impairment.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (hearingImpairmentDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default hearingImpairment if it's from default group
                        if(!hearingImpairmentDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            hearingImpairmentDao.delete(hearingImpairmentDao.read(id));
                        }else{
                            System.out.println("hasGroupRel");
                            return "lists/itemUsed";
                        }
                    }else{
                        HearingImpairmentGroupRel h = hearingImpairmentDao.getGroupRel(id,groupId);
                        if(!hearingImpairmentDao.isDefault(id)){ // delete only non default hearingImpairment
                            hearingImpairmentDao.delete(hearingImpairmentDao.read(id));
                        }
                        hearingImpairmentDao.deleteGroupRel(h);
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

    private void fillHearingImpairmentList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            hearingImpairmentList = hearingImpairmentDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                hearingImpairmentList = hearingImpairmentDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public HearingImpairmentDao getHearingImpairmentDao() {
        return hearingImpairmentDao;
    }

    public void setHearingImpairmentDao(HearingImpairmentDao hearingImpairmentDao) {
        this.hearingImpairmentDao = hearingImpairmentDao;
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











