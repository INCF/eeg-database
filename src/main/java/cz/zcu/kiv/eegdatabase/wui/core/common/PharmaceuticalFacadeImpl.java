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
 *   PharmaceuticalFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PharmaceuticalFacadeImpl implements PharmaceuticalFacade {

    protected Log log = LogFactory.getLog(getClass());

    PharmaceuticalService service;

    @Required
    public void setService(PharmaceuticalService service) {
        this.service = service;
    }

    @Override
    public Integer create(Pharmaceutical newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Pharmaceutical read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Pharmaceutical> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Pharmaceutical transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Pharmaceutical persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Pharmaceutical> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Pharmaceutical> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Pharmaceutical> getUnique(Pharmaceutical example) {
        return service.getUnique(example);
    }

    @Override
    public boolean canSaveTitle(String title) {
        return service.canSaveTitle(title);
    }

    @Override
    public void createGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        service.createGroupRel(persistent, researchGroup);
    }

    @Override
    public List<Pharmaceutical> getItemsForList() {
        return service.getItemsForList();
    }

    @Override
    public List<Pharmaceutical> getRecordsByGroup(int groupId) {
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
    public void deleteGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        service.deleteGroupRel(persistent, researchGroup);
    }
}
