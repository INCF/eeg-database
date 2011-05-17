package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Delegate class for multicontroller which processes groups section.
 *
 * @author Jindrich Pergler
 */
public class GroupMultiController extends MultiActionController {

    private AuthorizationManager auth;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao;

    public ModelAndView editGroupRole(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("groups/editGroupRole");
        return mav;
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("groups/list");
        List<ResearchGroup> list = researchGroupDao.getAllRecords();
        mav.addObject("groupList", list);
        return mav;
    }

    public ModelAndView myGroups(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("groups/myGroupsList");
        List<ResearchGroup> ownedList = researchGroupDao.getResearchGroupsWhereOwner(personDao.getLoggedPerson());
        mav.addObject("ownedList", ownedList);
        List<ResearchGroup> memberList = researchGroupDao.getResearchGroupsWhereMember(personDao.getLoggedPerson());
        mav.addObject("memberList", memberList);
        return mav;
    }

    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("groups/detail");
        int id = Integer.parseInt(request.getParameter("groupId"));

        mav.addObject("userIsExperimenterInGroup", auth.userIsExperimenterInGroup(id));
        mav.addObject("userIsAdminInGroup", auth.userIsAdminInGroup(id));

        ResearchGroup group = researchGroupDao.read(id);
        mav.addObject("researchGroup", group);
        return mav;
    }

    public ModelAndView listOfMembers(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("groups/listOfMembers");

        int groupId = 0;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException e) {
        }

        if (!auth.userIsExperimenterInGroup(groupId)) {
            mav.setViewName("redirect:/groups/detail.html?groupId=" + groupId);
            return mav;
        }
//
        mav.addObject("userIsAdminInGroup", auth.userIsAdminInGroup(groupId));
        mav.addObject("memberList", researchGroupDao.getListOfGroupMembers(groupId));
        mav.addObject("groupId", groupId);
        mav.addObject("groupTitle", researchGroupDao.read(groupId).getTitle());
        return mav;
    }

    public ModelAndView changeRole(HttpServletRequest request, HttpServletResponse response) {
        // If we don't get the group id, redirect to list of groups
        ModelAndView mav = new ModelAndView("redirect:/groups/list.html");

        int groupId = 0;
        int personId = 0;
        String role = "";
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
            // Now we know the group id, so we change the redirect to the list of members
            mav.setViewName("redirect:/groups/list-of-members.html?groupId=" + groupId);
            personId = Integer.parseInt(request.getParameter("personId"));
            role = request.getParameter("role");
        } catch (NumberFormatException e) {
            return mav;
        }

        if (!auth.userIsAdminInGroup(groupId)) {
            // If user has no permission for changing role, we will return back to the list of members
            return mav;
        }

        if ((!role.equals(Util.GROUP_ADMIN))
                && (!role.equals(Util.GROUP_EXPERIMENTER))
                && (!role.equals(Util.GROUP_READER))
                && (!role.equals("REMOVE"))) {
            // If specified role is not correct, return MAV so we return back with no changes
            return mav;
        }

        // Now we can make changes
        ResearchGroupMembership membership = membershipDao.read(new ResearchGroupMembershipId(personId, groupId));
        if (role.equals("REMOVE")) {
            membershipDao.delete(membership);
        } else {
            membership.setAuthority(role);
        }

        return mav;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> getMembershipDao() {
        return membershipDao;
    }

    public void setMembershipDao(GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> membershipDao) {
        this.membershipDao = membershipDao;
    }
}
