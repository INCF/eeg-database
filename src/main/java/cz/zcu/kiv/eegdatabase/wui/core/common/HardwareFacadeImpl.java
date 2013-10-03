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
 *   HardwareFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class HardwareFacadeImpl implements HardwareFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    HardwareService service;
    
    @Required
    public void setService(HardwareService service) {
        this.service = service;
    }

    @Override
    public Integer create(Hardware newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Hardware read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Hardware> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Hardware transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Hardware persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Hardware> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Hardware> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Hardware> getUnique(Hardware example) {
        return service.getUnique(example);
    }

    @Override
    public void createDefaultRecord(Hardware hardware) {
        service.createDefaultRecord(hardware);
    }

    @Override
    public void createGroupRel(HardwareGroupRel hardwareGroupRel) {
        service.createGroupRel(hardwareGroupRel);
    }

    @Override
    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup) {
        service.createGroupRel(hardware, researchGroup);
    }

    @Override
    public List<Hardware> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Hardware> getRecordsNewerThan(long oracleScn) {
        return service.getRecordsNewerThan(oracleScn);
    }

    @Override
    public List<Hardware> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public List<Hardware> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel) {
        service.deleteGroupRel(hardwareGroupRel);
    }

    @Override
    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId) {
        return service.getGroupRel(hardwareId, researchGroupId);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int hwId) {
        return service.canSaveTitle(title, groupId, hwId);
    }

    @Override
    public boolean canSaveNewTitle(String title, int groupId) {
        return service.canSaveNewTitle(title, groupId);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int hwId) {
        return service.canSaveDefaultTitle(title, hwId);
    }
}
