package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddHearingImpairmentController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;

    public AddHearingImpairmentController() {
        setCommandClass(AddHearingImpairmentCommand.class);
        setCommandName("addHearingDefect");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddHearingImpairmentCommand data = (AddHearingImpairmentCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editation of existing impairment
            int id = Integer.parseInt(idString);

            log.debug("Loading hearing impairment to the command object for editing.");
            HearingImpairment impairment = hearingImpairmentDao.read(id);

            data.setHearingImpairmentId(id);
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
        AddHearingImpairmentCommand data = (AddHearingImpairmentCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        HearingImpairment hearingImpairment;
        if (data.getHearingImpairmentId() > 0) {
            // Editing
            log.debug("Editing existing object.");
            hearingImpairment = hearingImpairmentDao.read(data.getHearingImpairmentId());
            hearingImpairment.setDescription(data.getDescription());
            hearingImpairmentDao.update(hearingImpairment);
        } else {
            // Creating new
            log.debug("Creating new object.");
            hearingImpairment = new HearingImpairment();
            hearingImpairment.setDescription(data.getDescription());
            hearingImpairmentDao.create(hearingImpairment);
        }


        log.debug("Returning MAV.");
        return mav;
    }
}
