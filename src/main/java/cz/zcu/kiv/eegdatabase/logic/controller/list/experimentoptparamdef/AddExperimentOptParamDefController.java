package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddExperimentOptParamDefController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private ExperimentOptParamDefDao experimentOptParamDefDao;

    public AddExperimentOptParamDefController() {
        setCommandClass(AddExperimentOptParamDefCommand.class);
        setCommandName("addMeasurationAdditionalParam");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddExperimentOptParamDefCommand data = (AddExperimentOptParamDefCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editing
            int id = Integer.parseInt(idString);

            log.debug("Loading optional parameter definition for experiment for editing.");
            ExperimentOptParamDef definition = experimentOptParamDefDao.read(id);

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

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data.");
        AddExperimentOptParamDefCommand data = (AddExperimentOptParamDefCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        ExperimentOptParamDef def;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editign existing optional parameter for experiments object.");
            def = experimentOptParamDefDao.read(data.getId());
            def.setParamName(data.getParamName());
            def.setParamDataType(data.getParamDataType());
            experimentOptParamDefDao.update(def);
        } else {
            log.debug("Creating new object");
            def = new ExperimentOptParamDef();
            def.setParamName(data.getParamName());
            def.setParamDataType(data.getParamDataType());
            experimentOptParamDefDao.create(def);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
