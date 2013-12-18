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
 *   AddDataFileController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    private static final int MAX_MIMETYPE_LENGTH = 40;

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
        return adc;
    }

    /**
     * Processing of the valid form
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        log.debug("Processing form data.");
        AddDataFileCommand addDataCommand = (AddDataFileCommand) command;
        MultipartHttpServletRequest mpRequest = (MultipartHttpServletRequest) request;
        // the map containing file names mapped to files
        Map m = mpRequest.getFileMap();
        Set set = m.keySet();
        for (Object key : set) {
            MultipartFile file = (MultipartFile) m.get(key);

            if (file == null) {
                log.error("No file was uploaded!");
            } else {
                log.debug("Creating measuration with ID " + addDataCommand.getMeasurationId());
                Experiment experiment = new Experiment();
                experiment.setExperimentId(addDataCommand.getMeasurationId());
                if (file.getOriginalFilename().endsWith(".zip")) {
                    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));
                    ZipEntry en = zis.getNextEntry();
                    while (en != null) {
                        if (en.isDirectory()) {
                            en = zis.getNextEntry();
                            continue;
                        }
                        DataFile data = new DataFile();
                        data.setExperiment(experiment);
                        String name[] = en.getName().split("/");
                        data.setFilename(name[name.length - 1]);
                        data.setDescription(addDataCommand.getDescription());
                        data.setFileContent(SignalProcessingUtils.extractZipEntry(zis));
                        String[] partOfName = en.getName().split("[.]");
                        data.setMimetype(partOfName[partOfName.length - 1]);
                        dataFileDao.create(data);
                        en = zis.getNextEntry();
                    }
                } else {
                    log.debug("Creating new Data object.");
                    DataFile data = new DataFile();
                    data.setExperiment(experiment);

                    log.debug("Original name of uploaded file: " + file.getOriginalFilename());
                    String filename = file.getOriginalFilename().replace(" ", "_");
                    data.setFilename(filename);

                    log.debug("MIME type of the uploaded file: " + file.getContentType());
                    if (file.getContentType().length() > MAX_MIMETYPE_LENGTH) {
                        int index = filename.lastIndexOf(".");
                        data.setMimetype(filename.substring(index));
                    } else {
                        data.setMimetype(file.getContentType());
                    }

                    log.debug("Parsing the sapmling rate.");
                    data.setDescription(addDataCommand.getDescription());

                    log.debug("Setting the binary data to object.");
                    data.setFileContent(file.getBytes());

                    dataFileDao.create(data);
                    log.debug("Data stored into database.");
                }
            }
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
        if ((!auth.userIsOwnerOrCoexperimenter(data.getMeasurationId())) && (!auth.isAdmin())) {
            errors.reject("error.mustBeOwnerOfExperimentOrCoexperimenter");
        } else {

            if (data.getMeasurationId() == 0) {
                log.debug("Measuration ID not inserted!");
            } else {
                log.debug("Measuration ID is present.");
            }

            // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "samplingRate", "required.samplingRate");


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
