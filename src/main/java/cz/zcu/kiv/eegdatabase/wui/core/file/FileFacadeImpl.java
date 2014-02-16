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
 *   FileFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.file;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;

public class FileFacadeImpl implements FileFacade {

    protected Log log = LogFactory.getLog(getClass());

    FileService fileService;

    @Required
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public Integer create(DataFile newInstance) {
        return fileService.create(newInstance);
    }

    @Override
    public DataFile read(Integer id) {
        return fileService.read(id);
    }

    @Override
    public List<DataFile> readByParameter(String parameterName, Object parameterValue) {
        return fileService.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(DataFile transientObject) {
        fileService.update(transientObject);
    }

    @Override
    public void delete(DataFile persistentObject) {
        fileService.delete(persistentObject);
    }

    @Override
    public List<DataFile> getAllRecords() {
        return fileService.getAllRecords();
    }

    @Override
    public List<DataFile> getRecordsAtSides(int first, int max) {
        return fileService.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return fileService.getCountRecords();
    }

    @Override
    public List<DataFile> getUnique(DataFile example) {
        return fileService.getUnique(example);
    }

    /**
     * Method prepared file with file Id. File data is copied from database into byte array inside DataFileDTO.
     */
    @Override
    public FileDTO getFile(int fileId) {
        return fileService.getFile(fileId);
    }
}
