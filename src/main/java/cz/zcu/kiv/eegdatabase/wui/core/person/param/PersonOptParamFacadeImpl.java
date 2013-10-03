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
 *   PersonOptParamFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.person.param;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PersonOptParamFacadeImpl implements PersonOptParamFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    PersonOptParamService service;
    
    @Required
    public void setService(PersonOptParamService service) {
        this.service = service;
    }

    @Override
    public Integer create(PersonOptParamDef newInstance) {
        return service.create(newInstance);
    }

    @Override
    public PersonOptParamDef read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<PersonOptParamDef> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(PersonOptParamDef transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(PersonOptParamDef persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<PersonOptParamDef> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<PersonOptParamDef> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<PersonOptParamDef> getUnique(PersonOptParamDef example) {
        return service.getUnique(example);
    }

    @Override
    public List<PersonOptParamDef> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public List<PersonOptParamDef> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public void createDefaultRecord(PersonOptParamDef personOptParamDef) {
        service.createDefaultRecord(personOptParamDef);
    }

    @Override
    public List<PersonOptParamDef> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        service.deleteGroupRel(personOptParamDefGroupRel);
    }

    @Override
    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId) {
        return service.getGroupRel(personOptParamDefId, researchGroupId);
    }

    @Override
    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        service.createGroupRel(personOptParamDefGroupRel);
    }

    @Override
    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup) {
        service.createGroupRel(personOptParamDef, researchGroup);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public PersonOptParamValId create(PersonOptParamVal newInstance) {
        return service.create(newInstance);
    }

    @Override
    public PersonOptParamVal read(PersonOptParamValId id) {
        return service.read(id);
    }

    @Override
    public List<PersonOptParamVal> readValueByParameter(String parameterName, int parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public List<PersonOptParamVal> readValueByParameter(String parameterName, String parameterValue) {
        return service.readValueByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(PersonOptParamVal transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(PersonOptParamVal persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<PersonOptParamVal> getAllValueRecords() {
        return service.getAllValueRecords();
    }

    @Override
    public List<PersonOptParamVal> getValueRecordsAtSides(int first, int max) {
        return service.getValueRecordsAtSides(first, max);
    }

    @Override
    public int getValueCountRecords() {
        return service.getValueCountRecords();
    }

}
