package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddHardwareController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private HardwareDao hardwareDao;

    public AddHardwareController() {
        setCommandClass(AddHardwareCommand.class);
        setCommandName("addHardware");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddHardwareCommand data = (AddHardwareCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editation of existing hardware
            int id = Integer.parseInt(idString);

            log.debug("Loading hardware to the command object for editing.");
            Hardware hardware = hardwareDao.read(id);

            data.setId(id);
            data.setDescription(hardware.getDescription());
            data.setTitle(hardware.getTitle());
            data.setType(hardware.getType());
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
        AddHardwareCommand data = (AddHardwareCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        Hardware hardware;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing hardware object.");
            hardware = hardwareDao.read(data.getId());
            hardware.setDescription(data.getDescription());
            hardware.setTitle(data.getTitle());
            hardware.setType(data.getType());
            hardwareDao.update(hardware);
        } else {
            // Creating new
            log.debug("Creating new hardware object.");
            hardware = new Hardware();
            hardware.setTitle(data.getTitle());
            hardware.setType(data.getType());
            hardware.setDescription(data.getDescription());
            hardwareDao.create(hardware);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
