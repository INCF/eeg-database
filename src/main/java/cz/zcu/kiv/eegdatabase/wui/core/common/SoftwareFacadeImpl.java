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
 *   SoftwareFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareFacadeImpl implements SoftwareFacade{

    protected Log log = LogFactory.getLog(getClass());

    SoftwareService service;

    @Required
    public void setService(SoftwareService service) {
        this.service = service;
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int swId) {
        return service.canSaveDefaultTitle(title, swId);
    }

    @Override
    public void createDefaultRecord(Software software) {
        service.createDefaultRecord(software);
    }

    @Override
    public List<Software> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public Integer create(Software newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Software read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Software> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Software transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Software persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Software> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Software> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Software> getUnique(Software example) {
        return service.getUnique(example);
    }

    @Override
    public void createGroupRel(Software persistent, ResearchGroup researchGroup) {
        service.createGroupRel(persistent, researchGroup);
    }

    @Override
    public List<Software> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Software> getRecordsByGroup(int groupId) {
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
    public void deleteGroupRel(Software persistent, ResearchGroup researchGroup) {
        service.deleteGroupRel(persistent, researchGroup);
    }
    
    @Override
    public boolean canSaveTitle(String title, int researchGroupId, int softwareId) {
        return service.canSaveTitle(title, researchGroupId, softwareId);
    }

    @Override
    public boolean isDefault(int swId) {
        return service.isDefault(swId);
    }
}
