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
 *   ProjectTypeFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class ProjectTypeFacadeImpl implements ProjectTypeFacade{

    protected Log log = LogFactory.getLog(getClass());

    ProjectTypeService service;

    @Required
    public void setService(ProjectTypeService service) {
        this.service = service;
    }

    @Override
    public Integer create(ProjectType newInstance) {
        return service.create(newInstance);
    }

    @Override
    public ProjectType read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<ProjectType> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }


    @Override
    public void update(ProjectType transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(ProjectType persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<ProjectType> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<ProjectType> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<ProjectType> getUnique(ProjectType example) {
        return service.getUnique(example);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int projectId) {
        return service.canSaveTitle(title, groupId, projectId);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int projectId) {
        return service.canSaveDefaultTitle(title, projectId);
    }

    @Override
    public void createGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        service.createGroupRel(persistent, researchGroup);
    }

    @Override
    public List<ProjectType> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<ProjectType> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        service.deleteGroupRel(persistent, researchGroup);
    }

    @Override
    public void createDefaultRecord(ProjectType project) {
        service.createDefaultRecord(project);
    }
}
