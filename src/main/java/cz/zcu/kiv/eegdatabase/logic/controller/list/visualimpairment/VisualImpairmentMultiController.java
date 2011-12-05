package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairmentGroupRel;
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
public class VisualImpairmentMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private List<ResearchGroup> researchGroupList;
    private List<VisualImpairment> visualImpairmentList;
    private final int DEFAULT_ID = -1;

    @RequestMapping(value="lists/visual-impairments/list.html",method=RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest,ModelMap model, HttpServletRequest request){
        log.debug("Processing visual defect list controller");
        SelectGroupCommand selectGroupCommand= new SelectGroupCommand();
        String defaultVisualImpairment = messageSource.getMessage("label.defaultVisualImpairments", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultVisualImpairment);
        String idString = webRequest.getParameter("groupid");
        if(auth.isAdmin()){
            if (idString != null) {
                int id = Integer.parseInt(idString);
                if(id!=DEFAULT_ID){
                    fillVisualImpairmentList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                   fillVisualImpairmentList(DEFAULT_ID);
                    selectGroupCommand.setResearchGroupId(DEFAULT_ID);
                }

            }else{
                fillVisualImpairmentList(DEFAULT_ID);
                selectGroupCommand.setResearchGroupId(DEFAULT_ID);
            }
            model.addAttribute("userIsExperimenter", true);
        }else{
            if(!researchGroupList.isEmpty()){
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    fillVisualImpairmentList(id);
                    selectGroupCommand.setResearchGroupId(id);
                }else{
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    fillVisualImpairmentList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            }else{
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand",selectGroupCommand);
        model.addAttribute("visualImpairmentList", visualImpairmentList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/eyesDefect/list";
    }

    @RequestMapping(value="lists/visual-impairments/list.html",method=RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request){
        String defaultVisualImpairment = messageSource.getMessage("label.defaultVisualImpairments", null, RequestContextUtils.getLocale(request));
        fillAuthResearchGroupList(defaultVisualImpairment);
        if(!researchGroupList.isEmpty()){
            fillVisualImpairmentList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("visualImpairmentList", visualImpairmentList);
        return "lists/eyesDefect/list";
    }

    @RequestMapping(value="lists/visual-impairments/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting visual impairment.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (visualImpairmentDao.canDelete(id)) {
                if(idString2 !=null){
                    int groupId = Integer.parseInt(idString2);

                    if(groupId==DEFAULT_ID){ // delete default visualImpairment if it's from default group
                        if(!visualImpairmentDao.hasGroupRel(id)){ // delete only if it doesn't have group relationship
                            visualImpairmentDao.delete(visualImpairmentDao.read(id));
                        }else{
                            System.out.println("hasGroupRel");
                            return "lists/itemUsed";
                        }
                    }else{
                        VisualImpairmentGroupRel h = visualImpairmentDao.getGroupRel(id,groupId);
                        if(!visualImpairmentDao.isDefault(id)){ // delete only non default visualImpairment
                            visualImpairmentDao.delete(visualImpairmentDao.read(id));
                        }
                        visualImpairmentDao.deleteGroupRel(h);
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

    private void fillVisualImpairmentList(int selectedGroupId){
        if(selectedGroupId == DEFAULT_ID){
            visualImpairmentList = visualImpairmentDao.getDefaultRecords();
        }else{
            if(!researchGroupList.isEmpty()){
                visualImpairmentList = visualImpairmentDao.getRecordsByGroup(selectedGroupId);
            }
        }
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public VisualImpairmentDao getVisualImpairmentDao() {
        return visualImpairmentDao;
    }

    public void setVisualImpairmentDao(VisualImpairmentDao visualImpairmentDao) {
        this.visualImpairmentDao = visualImpairmentDao;
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











