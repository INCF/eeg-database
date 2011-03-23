package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for adding data file to database
 *
 * @author Jindra
 */
public class AddDataFileController
        extends SimpleFormController
        implements Validator {

    private static final String PARAM_ID = "experimentId";
    private AuthorizationManager auth;
    private Log log = LogFactory.getLog(getClass());
    private GenericDao<DataFile, Integer> dataFileDao;

    public AddDataFileController() {
        setCommandClass(AddDataFileCommand.class);
        setCommandName("addData");
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());
        return mav;
    }

    /**
     * Setting the measuratioId value into the form
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddDataFileCommand adc = (AddDataFileCommand) super.formBackingObject(request);
        String measurationId = request.getParameter(PARAM_ID);
        if (measurationId != null) {
            adc.setMeasurationId(Integer.parseInt(measurationId));
        }
        adc.setSamplingRate("1000");
        return adc;
    }

    /**
     * Processing of the valid form
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing form data.");
        AddDataFileCommand addDataCommand = (AddDataFileCommand) command;

        MultipartFile file = addDataCommand.getDataFile();

        if (file == null) {
            log.error("No file was uploaded!");
        } else {
            log.debug("Creating measuration with ID " + addDataCommand.getMeasurationId());
            Experiment experiment = new Experiment();
            experiment.setExperimentId(addDataCommand.getMeasurationId());

            log.debug("Creating new Data object.");
            DataFile data = new DataFile();
            data.setExperiment(experiment);

            log.debug("Original name of uploaded file: " + file.getOriginalFilename());
            data.setFilename(file.getOriginalFilename());

            log.debug("MIME type of the uploaded file: " + file.getContentType());
            data.setMimetype(file.getContentType());

            log.debug("Parsing the sapmling rate.");
            double samplingRate = Double.parseDouble(addDataCommand.getSamplingRate());
            data.setSamplingRate(samplingRate);

            log.debug("Setting the binary data to object.");
            data.setFileContent(Hibernate.createBlob(addDataCommand.getDataFile().getBytes()));

            dataFileDao.create(data);
            log.debug("Data stored into database.");
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView("redirect:/experiments/detail.html?experimentId=" + addDataCommand.getMeasurationId());
        return mav;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(AddDataFileCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddDataFileCommand data = (AddDataFileCommand) command;

        // First check the permission
        if (!auth.userIsOwnerOrCoexperimenter(data.getMeasurationId())) {
            errors.reject("error.mustBeOwnerOfExperimentOrCoexperimenter");
        } else {

            if (data.getMeasurationId() == 0) {
                log.debug("Measuration ID not inserted!");
            } else {
                log.debug("Measuration ID is present.");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "samplingRate", "required.samplingRate");

            try {
                Double.parseDouble(data.getSamplingRate());
            } catch (NumberFormatException ex) {
                errors.rejectValue("samplingRate", "invalid.samplingRate");
                log.debug("Sampling rate is not in parseable format!");
            }

            if (data.getDataFile().isEmpty()) {
                errors.rejectValue("dataFile", "required.dataFile");
                log.debug("No data file was inserted!");
            }
        }

    }

    public GenericDao<DataFile, Integer> getDataFileDao() {
        return dataFileDao;
    }

    public void setDataFileDao(GenericDao<DataFile, Integer> dataFileDao) {
        this.dataFileDao = dataFileDao;

    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
