package cz.zcu.kiv.eegdatabase.logic.controller.list.personoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddPersonOptParamDefController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private PersonOptParamDefDao personOptParamDefDao;

    public AddPersonOptParamDefController() {
        setCommandClass(AddPersonOptParamDefCommand.class);
        setCommandName("addPersonAdditionalParam");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddPersonOptParamDefCommand data = (AddPersonOptParamDefCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editing
            int id = Integer.parseInt(idString);

            log.debug("Loading optional parameter definition for people for editing.");
            PersonOptParamDef definition = personOptParamDefDao.read(id);

            data.setId(id);
            data.setParamName(definition.getParamName());
            data.setParamDataType(definition.getParamDataType());
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
        AddPersonOptParamDefCommand data = (AddPersonOptParamDefCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        PersonOptParamDef def;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing optional parameter for people object.");
            def = personOptParamDefDao.read(data.getId());
            def.setParamName(data.getParamName());
            def.setParamDataType(data.getParamDataType());
            personOptParamDefDao.update(def);
        } else {
            // Creating new
            log.debug("Creating new object");
            def = new PersonOptParamDef();
            def.setParamName(data.getParamName());
            def.setParamDataType(data.getParamDataType());
            personOptParamDefDao.create(def);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
