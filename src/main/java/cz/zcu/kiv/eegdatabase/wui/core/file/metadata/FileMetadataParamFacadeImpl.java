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
 *   FileMetadataParamFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.file.metadata;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class FileMetadataParamFacadeImpl implements FileMetadataParamFacade {

    protected Log log = LogFactory.getLog(getClass());

    FileMetadataParamService service;

    @Required
    public void setService(FileMetadataParamService service) {
        this.service = service;
    }

    @Override
    public Integer create(FileMetadataParamDef newInstance) {
        return service.create(newInstance);
    }

    @Override
    public FileMetadataParamDef read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<FileMetadataParamDef> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }
    
    @Override
    public void update(FileMetadataParamDef transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(FileMetadataParamDef persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<FileMetadataParamDef> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<FileMetadataParamDef> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<FileMetadataParamDef> getUnique(FileMetadataParamDef example) {
        return service.getUnique(example);
    }

    @Override
    public List<FileMetadataParamDef> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public List<FileMetadataParamDef> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public void createDefaultRecord(FileMetadataParamDef fileMetadataParamDef) {
        service.createDefaultRecord(fileMetadataParamDef);
    }

    @Override
    public List<FileMetadataParamDef> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        service.deleteGroupRel(fileMetadataParamDefGroupRel);
    }

    @Override
    public FileMetadataParamDefGroupRel getGroupRel(int fileMetadataParamDefId, int researchGroupId) {
        return service.getGroupRel(fileMetadataParamDefId, researchGroupId);
    }

    @Override
    public void createGroupRel(FileMetadataParamDefGroupRel fileMetadataParamDefGroupRel) {
        service.createGroupRel(fileMetadataParamDefGroupRel);
    }

    @Override
    public void createGroupRel(FileMetadataParamDef fileMetadataParamDef, ResearchGroup researchGroup) {
        service.createGroupRel(fileMetadataParamDef, researchGroup);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }
    
    @Override
    public FileMetadataParamValId create(FileMetadataParamVal newInstance) {
        return service.create(newInstance);
    }

    @Override
    public FileMetadataParamVal read(FileMetadataParamValId id) {
        return service.read(id);
    }

    @Override
    public List<FileMetadataParamVal> readValueByParameter(String parameterName, int parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public List<FileMetadataParamVal> readValueByParameter(String parameterName, String parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(FileMetadataParamVal transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(FileMetadataParamVal persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<FileMetadataParamVal> getAllValueRecords() {
        return service.getAllValueRecords();
    }

    @Override
    public List<FileMetadataParamVal> getValueRecordsAtSides(int first, int max) {
        return service.getValueRecordsAtSides(first, max);
    }

    @Override
    public int getValueCountRecords() {
        return service.getCountRecords();
    }

}
