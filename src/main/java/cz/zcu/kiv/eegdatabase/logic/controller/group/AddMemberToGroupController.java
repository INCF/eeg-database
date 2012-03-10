/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.service.DataService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jindra
 */
public class AddMemberToGroupController
        extends SimpleFormController
        implements Validator {

    private AuthorizationManager auth;
    @Autowired
    private DataService dataService;
    @Autowired
    private PersonDao personDao;

    public AddMemberToGroupController() {
        setCommandClass(AddMemberToGroupCommand.class);
        setCommandName("addMemberToGroup");
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        int groupId = 0;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/groups/list.html");
        }
        mav.addObject("groupId", groupId);
        mav.addObject("groupTitle", dataService.getResearchGroupTitle(groupId));
        GroupMultiController.setPermissionToRequestGroupRole(mav, personDao.getLoggedPerson());

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView mav = super.onSubmit(request, response, command, errors);

        int groupId = 0;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/groups/list.html");
        }

        if (!auth.userIsAdminInGroup(groupId)) {
            return new ModelAndView("redirect:/groups/detail.html?groupId=" + groupId);
        }

        AddMemberToGroupCommand data = (AddMemberToGroupCommand) command;


        // Now we can add the user into group
        dataService.addMemberToResearchGroup(data.getUserName(),
                data.getGroupFormId(),
                data.getUserRole());

        mav = new ModelAndView("redirect:/groups/list-of-members.html?groupId=" + groupId);

        return mav;
    }

    public boolean supports(Class type) {
        return type.equals(AddMemberToGroupCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddMemberToGroupCommand data = (AddMemberToGroupCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "required.field");
        if (data.getUserRole().equals("-1")) {
            errors.rejectValue("userRole", "required.userRole");
        } else if (!(data.getUserRole().equals(Util.GROUP_ADMIN)
                || data.getUserRole().equals(Util.GROUP_EXPERIMENTER)
                || data.getUserRole().equals(Util.GROUP_READER))) {
            errors.rejectValue("userRole", "invalid.userRole");
        }

        if (!dataService.usernameExists(data.getUserName())) {
            errors.rejectValue("userName", "invalid.userNameDoesNotExist");

        } else if (dataService.userNameInGroup(data.getUserName(), data.getGroupFormId())) {
            errors.rejectValue("userName", "invalid.userNameAlreadyInGroup");
        }
    }
}
