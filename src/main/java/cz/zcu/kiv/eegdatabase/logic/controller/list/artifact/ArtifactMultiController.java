package cz.zcu.kiv.eegdatabase.logic.controller.list.artifact;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.list.SelectGroupCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Honza
 * Date: 24.6.12
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes("selectGroupCommand")
public class ArtifactMultiController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private GenericListDao<Artifact> artifactDao;
    @Autowired
    private PersonDao personDao;

    private List<ResearchGroup> researchGroupList;
    private List<Artifact> artifactList;

    @RequestMapping(value = "lists/artifact-definitions/list.html", method = RequestMethod.GET)
    public String showSelectForm(WebRequest webRequest, ModelMap model, HttpServletRequest request) {
        log.debug("Processing artifact list controller");
        SelectGroupCommand selectGroupCommand = new SelectGroupCommand();
        researchGroupList = fillAuthResearchGroupList();
        String idString = webRequest.getParameter("groupid");
        if (auth.isAdmin()) {
            int id = Integer.parseInt(idString);
            artifactList = fillArtifactList(id);
            selectGroupCommand.setResearchGroupId(id);
            model.addAttribute("userIsExperimenter", true);
        } else {
            if (!researchGroupList.isEmpty()) {
                if (idString != null) {
                    int id = Integer.parseInt(idString);
                    artifactList = fillArtifactList(id);
                    selectGroupCommand.setResearchGroupId(id);
                } else {
                    int myGroup = researchGroupList.get(0).getResearchGroupId();
                    artifactList = fillArtifactList(myGroup);
                    selectGroupCommand.setResearchGroupId(myGroup);
                }
                model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
            } else {
                model.addAttribute("userIsExperimenter", false);
            }
        }
        model.addAttribute("selectGroupCommand", selectGroupCommand);
        model.addAttribute("artifactList", artifactList);
        model.addAttribute("researchGroupList", researchGroupList);


        return "lists/artifact/list";
    }

    @RequestMapping(value = "lists/artifact-definitions/list.html", method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute("selectGroupCommand") SelectGroupCommand selectGroupCommand, ModelMap model, HttpServletRequest request) {
        // String defaultHardware = messageSource.getMessage("label.defaultHardware", null, RequestContextUtils.getLocale(request));
        researchGroupList = fillAuthResearchGroupList();
        if (!researchGroupList.isEmpty()) {
            artifactList = fillArtifactList(selectGroupCommand.getResearchGroupId());
        }
        boolean canEdit = auth.isAdmin() || auth.userIsExperimenter();
        model.addAttribute("userIsExperimenter", canEdit);
        model.addAttribute("researchGroupList", researchGroupList);
        model.addAttribute("artifactList", artifactList);
        return "lists/artifact/list";
    }

    @RequestMapping(value = "lists/artifact-definitions/delete.html")
    public String delete(@RequestParam("id") String idString, @RequestParam("groupid") String idString2) {
        log.debug("Deleting hardware.");
        if (idString != null) {
            int id = Integer.parseInt(idString);

            if (artifactDao.canDelete(id)) {
                if (idString2 != null) {
                    int groupId = Integer.parseInt(idString2);
                    artifactDao.deleteGroupRel(artifactDao.read(id), researchGroupDao.read(groupId));

                }
            } else {
                System.out.println("cannotDelete");
                return "lists/itemUsed";
            }
        }

        return "lists/itemDeleted";
    }

    private List<ResearchGroup> fillAuthResearchGroupList() {
        Person loggedUser = personDao.getLoggedPerson();
        if (loggedUser.getAuthority().equals("ROLE_ADMIN")) {
            return researchGroupDao.getAllRecords();
        } else {
            return researchGroupDao.getResearchGroupsWhereMember(loggedUser);
        }
    }

    private List<Artifact> fillArtifactList(int id) {
        if (!researchGroupList.isEmpty()) {
            return artifactDao.getRecordsByGroup(id);
        }
        return null;
    }
}
