package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddVisualImpairmentController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;

    public AddVisualImpairmentController() {
        setCommandClass(AddVisualImpairmentCommand.class);
        setCommandName("addEyesDefect");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddVisualImpairmentCommand data = (AddVisualImpairmentCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editation of existing impairment
            int id = Integer.parseInt(idString);

            log.debug("Loading visual impairment to the command object for editing.");
            VisualImpairment impairment = visualImpairmentDao.read(id);

            data.setVisualImpairmentId(id);
            data.setDescription(impairment.getDescription());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);
        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());
        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data.");
        AddVisualImpairmentCommand data = (AddVisualImpairmentCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        VisualImpairment visualImpairment;
        if (data.getVisualImpairmentId() > 0) {
            // Editing
            log.debug("Editing existing object.");
            visualImpairment = visualImpairmentDao.read(data.getVisualImpairmentId());
            visualImpairment.setDescription(data.getDescription());
            visualImpairmentDao.update(visualImpairment);
        } else {
            // Creating new
            log.debug("Creating new object.");
            visualImpairment = new VisualImpairment();
            visualImpairment.setDescription(data.getDescription());
            visualImpairmentDao.create(visualImpairment);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
