package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling requests upon Research group service.
 *
 * @author Petr Miko
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("groups")
public class ResearchGroupServiceController {

    @Autowired
    private ResearchGroupService groupService;

    /**
     * Getter of all research groups.
     *
     * @return list container of research groups
     */
    @RequestMapping(value = "/all")
    public ResearchGroupDataList getAllGroups() {
        return new ResearchGroupDataList(groupService.getAllGroups());
    }

    /**
     * Getter of user's research groups.
     *
     * @return list container of research groups
     */
    @RequestMapping(value = "/mine")
    public ResearchGroupDataList getMyGroups() {
        return new ResearchGroupDataList(groupService.getMyGroups());
    }
}
