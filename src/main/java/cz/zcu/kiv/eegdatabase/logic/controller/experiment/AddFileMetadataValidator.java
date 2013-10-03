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
 *   AddFileMetadataValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddFileMetadataValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddFileMetadataCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddFileMetadataCommand addFileMetadataCommand = (AddFileMetadataCommand) command;
        log.debug("Validating form for adding file metadata.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.paramValue");

        if (addFileMetadataCommand.getParamId() <= 0) {
            errors.rejectValue("paramId", "required.paramId");
        }

        FileMetadataParamVal f = fileMetadataDao.read(new FileMetadataParamValId(addFileMetadataCommand.getParamId(), addFileMetadataCommand.getDataId()));
        if (f != null) {
            errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
        }
    }

    public GenericDao<FileMetadataParamVal, FileMetadataParamValId> getFileMetadataDao() {
        return fileMetadataDao;
    }

    public void setFileMetadataDao(GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataDao) {
        this.fileMetadataDao = fileMetadataDao;
    }
}
