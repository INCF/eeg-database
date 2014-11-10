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
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeConf;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.wui.core.common.ElectrodeConfService;
import cz.zcu.kiv.eegdatabase.wui.core.file.metadata.FileMetadataParamService;
import cz.zcu.kiv.eegdatabase.wui.core.history.HistoryService;

public class FileFacadeImpl implements FileFacade {

    protected Log log = LogFactory.getLog(getClass());

    FileService fileService;
    HistoryService historyService;
    FileMetadataParamService fileMetadataService;
    ElectrodeConfService electrodeConfService;

    @Required
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    
    @Required
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
    
    @Required
    public void setFileMetadataService(FileMetadataParamService fileMetadataService) {
        this.fileMetadataService = fileMetadataService;
    }
    
    @Required
    public void setElectrodeConfService(ElectrodeConfService electrodeConfService) {
        this.electrodeConfService = electrodeConfService;
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
    @Transactional
    public void delete(DataFile persistentObject) {
        
        // In one transaction for fails.
        // delete all history records for that datafile
        List<History> historyForDataFile = historyService.readByParameter("dataFile.dataFileId", persistentObject.getDataFileId());
        if (historyForDataFile != null) {
            log.trace("delete History records: " + historyForDataFile.size());
            for (History tmp : historyForDataFile) {
                historyService.delete(tmp);
            }
        }
        // delete all file metadada parameter value for that datafile
        List<FileMetadataParamVal> metadataParamForDataFile = fileMetadataService.readValueByParameter("dataFile.dataFileId", persistentObject.getDataFileId());
        if (metadataParamForDataFile != null) {
            log.trace("delete FileMetadataParamVal records: " + metadataParamForDataFile.size());
            for (FileMetadataParamVal tmp : metadataParamForDataFile) {
                fileMetadataService.delete(tmp);
            }
        }
        
        List<ElectrodeConf> electrodeConf = electrodeConfService.readByParameter("descImg.dataFileId", persistentObject.getDataFileId());
        if(electrodeConf != null) {
            for (ElectrodeConf eConf : electrodeConf) {
                eConf.setDescImg(null);
                electrodeConfService.update(eConf);
            }
        }
        
        // delete datafile
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
