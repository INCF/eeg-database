package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.DataService;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jindrich Pergler
 */
public class CreateGroupController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private DataService dataService;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private AuthorizationManager auth;

    public CreateGroupController() {
        setCommandClass(CreateGroupCommand.class);
        setCommandName("createGroup");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateGroupCommand data = (CreateGroupCommand) super.formBackingObject(request);

        String idString = request.getParameter("groupId");

        if (idString != null) {
            // Editing existing hardware
            int id = Integer.parseInt(idString);

            log.debug("Loading research group to the command object for editing.");
            ResearchGroup researchGroup = researchGroupDao.read(id);

            data.setId(researchGroup.getResearchGroupId());
            data.setResearchGroupTitle(researchGroup.getTitle());
            data.setResearchGroupDescription(researchGroup.getDescription());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        String idString = request.getParameter("groupId");

        if (idString != null) {
            // Editing existing hardware
            int id = Integer.parseInt(idString);
            if (!auth.userIsAdminInGroup(id)) {
                mav.setViewName("redirect:/groups/detail.html?groupId=" + id);
            }
        } else {
            // Creating new group
            if (!auth.userIsExperimenter()) {
                mav.setViewName("redirect:/groups/list.html");
            }
        }

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
        log.debug("Processing form data with CreateGroupController.");
        ModelAndView mav = new ModelAndView();

        CreateGroupCommand data = (CreateGroupCommand) command;

        ResearchGroup researchGroup;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing research group.");
            researchGroup = researchGroupDao.read(data.getId());
            researchGroup.setTitle(data.getResearchGroupTitle());
            researchGroup.setDescription(data.getResearchGroupDescription());
        } else {
            // Creating new
            log.debug("Creating new research group.");
            researchGroup = dataService.createResearchGroup(data.getResearchGroupTitle(),
                    data.getResearchGroupDescription(),
                    ControllerUtils.getLoggedUserName());
        }


        mav.setViewName("redirect:/groups/detail.html?groupId=" + researchGroup.getResearchGroupId());

        log.debug("Returning MAV.");
        return mav;
    }
}

