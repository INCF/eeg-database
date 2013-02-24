package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("groups")
public class ResearchGroupServiceController {

    @Autowired
    private ResearchGroupService groupService;

    @RequestMapping(value = "/all")
    public ResearchGroupDataList getAllGroups() {
        return new ResearchGroupDataList(groupService.getAllGroups());
    }

    @RequestMapping(value = "/mine")
    public ResearchGroupDataList getMyGroups() {
        return new ResearchGroupDataList(groupService.getMyGroups());
    }
}
