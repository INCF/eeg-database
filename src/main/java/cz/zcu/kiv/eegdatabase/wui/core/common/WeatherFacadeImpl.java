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
 *   WeatherFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class WeatherFacadeImpl implements WeatherFacade {

    protected Log log = LogFactory.getLog(getClass());

    WeatherService service;

    @Required
    public void setService(WeatherService service) {
        this.service = service;
    }

    @Override
    public Integer create(Weather newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Weather read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Weather> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Weather transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Weather persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Weather> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Weather> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Weather> getUnique(Weather example) {
        return service.getUnique(example);
    }

    @Override
    public List<Weather> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Weather> getRecordsNewerThan(long oracleScn) {
        return service.getRecordsNewerThan(oracleScn);
    }

    @Override
    public boolean canSaveDescription(String description, int groupId, int weatherId) {
        return service.canSaveDescription(description, groupId, weatherId);
    }

    @Override
    public boolean canSaveDefaultDescription(String description, int weatherId) {
        return service.canSaveDefaultDescription(description, weatherId);
    }

    @Override
    public boolean canDelete(int id) {
        return service.canDelete(id);
    }

    @Override
    public boolean canSaveNewDescription(String description, int groupId) {
        return service.canSaveNewDescription(description, groupId);
    }

    @Override
    public void createDefaultRecord(Weather weather) {
        service.createDefaultRecord(weather);
    }

    @Override
    public void createGroupRel(WeatherGroupRel weatherGroupRel) {
        service.createGroupRel(weatherGroupRel);
    }

    @Override
    public void createGroupRel(Weather weather, ResearchGroup researchGroup) {
        service.createGroupRel(weather, researchGroup);
    }

    @Override
    public List<Weather> getRecordsByGroup(int groupId) {
        return service.getRecordsByGroup(groupId);
    }

    @Override
    public List<Weather> getDefaultRecords() {
        return service.getDefaultRecords();
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public void deleteGroupRel(WeatherGroupRel weatherGroupRel) {
        service.deleteGroupRel(weatherGroupRel);
    }

    @Override
    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId) {
        return service.getGroupRel(weatherId, researchGroupId);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return service.hasGroupRel(id);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int weatherId) {
        return service.canSaveTitle(title, groupId, weatherId);
    }

    @Override
    public boolean canSaveNewTitle(String title, int groupId) {
        return service.canSaveNewTitle(title, groupId);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int weatherId) {
        return service.canSaveDefaultTitle(title, weatherId);
    }
}
