package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddFileMetadataParamDefController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private AuthorizationManager auth;
    @Autowired
    private FileMetadataParamDefDao fileMetadataParamDefDao;

    public AddFileMetadataParamDefController() {
        setCommandClass(AddFileMetadataParamDefCommand.class);
        setCommandName("addFileMetadataParam");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddFileMetadataParamDefCommand data = (AddFileMetadataParamDefCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");
        if (idString != null) {
            // Editing
            int id = Integer.parseInt(idString);

            log.debug("Loading file metadata parameter definition for editing.");
            FileMetadataParamDef definition = fileMetadataParamDefDao.read(id);

            data.setId(id);
            data.setParamDataType(definition.getParamDataType());
            data.setParamName(definition.getParamName());
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
        AddFileMetadataParamDefCommand data = (AddFileMetadataParamDefCommand) command;

        if (!auth.userIsExperimenter()) {
            mav.setViewName("lists/userNotExperimenter");
        }

        FileMetadataParamDef param;
        if (data.getId() > 0) {
            // Editing
            log.debug("Editing existing file metadata parameter definition.");
            param = fileMetadataParamDefDao.read(data.getId());
            param.setParamName(data.getParamName());
            param.setParamDataType(data.getParamDataType());
            fileMetadataParamDefDao.update(param);
        } else {
            // Creating new
            log.debug("Creating new object");
            param = new FileMetadataParamDef();
            param.setParamName(data.getParamName());
            param.setParamDataType(data.getParamDataType());
            fileMetadataParamDefDao.create(param);
        }

        log.debug("Returning MAV");
        return mav;
    }
}
