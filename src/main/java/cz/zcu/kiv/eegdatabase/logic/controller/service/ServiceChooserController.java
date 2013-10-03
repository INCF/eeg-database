/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ServiceChooserController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.service;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
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
