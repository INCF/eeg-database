package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.apache.jasper.tagplugins.jstl.core.Choose;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 16.9.11
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class ServiceChooserController extends SimpleFormController implements Validator {

        protected GenericDao<Experiment, Integer> experimentDao;

    public ServiceChooserController() {
        setCommandClass(ChooseCommand.class);
        setCommandName("chooseCmd");
    }

    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        int id = Integer.parseInt(request.getParameter("experimentId"));
        Experiment experiment = experimentDao.read(id);
        map.put("headers", SignalProcessingUtils.getHeaders(experiment));
        ServicesList[] services = ServicesList.values();
        map.put("services", services);
        map.put("experimentId", id);
        return map;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return super.formBackingObject(request);
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ChooseCommand cmd = (ChooseCommand) command;
        int id = Integer.parseInt(request.getParameter("experimentId"));
        String service = cmd.getService();
        ModelAndView mav = null;
        if (service.equals(ServicesList.MATCHING_PURSUIT.getName())) {
          mav = new ModelAndView("redirect:matchingForm.html?experimentId="+id);
        }
        if (service.equals(ServicesList.DISCRETE_WAVELET.getName())) {
          mav = new ModelAndView("redirect:waveletForm.html?experimentId="+id+"&type=DWT");
        }
        if (service.equals(ServicesList.CONTINUOUS_WAVELET.getName())) {
          mav = new ModelAndView("redirect:waveletForm.html?experimentId="+id+"&type=CWT");
        }
        if (service.equals(ServicesList.FAST_FOURIER.getName())) {
          mav = new ModelAndView("redirect:fourierForm.html?experimentId="+id);
        }
        mav.addObject("headerName", cmd.getHeaderName());
        return mav;

    }

    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public boolean supports(Class aClass) {
        return aClass.equals(ChooseCommand.class);
    }

    public void validate(Object o, Errors errors) {
        ChooseCommand cmd = (ChooseCommand) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "headerName", "required.header");

    }
}
