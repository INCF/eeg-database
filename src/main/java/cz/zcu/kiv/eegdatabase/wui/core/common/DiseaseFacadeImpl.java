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
 *   DiseaseFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class DiseaseFacadeImpl implements DiseaseFacade {

    DiseaseService diseaseService;

    @Required
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @Override
    public Integer create(Disease newInstance) {
        return diseaseService.create(newInstance);
    }

    @Override
    public Disease read(Integer id) {
        return diseaseService.read(id);
    }

    @Override
    public List<Disease> readByParameter(String parameterName, Object parameterValue) {
        return diseaseService.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Disease transientObject) {
        diseaseService.update(transientObject);
    }

    @Override
    public void delete(Disease persistentObject) {
        diseaseService.delete(persistentObject);
    }

    @Override
    public List<Disease> getAllRecords() {
        return diseaseService.getAllRecords();
    }

    @Override
    public List<Disease> getRecordsAtSides(int first, int max) {
        return diseaseService.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return diseaseService.getCountRecords();
    }

    @Override
    public List<Disease> getUnique(Disease example) {
        return diseaseService.getUnique(example);
    }

    @Override
    public boolean existsDisease(String name) {
        return diseaseService.existsDisease(name);
    }

    @Override
    public void createGroupRel(Disease persistent, ResearchGroup researchGroup) {
        diseaseService.createGroupRel(persistent, researchGroup);
    }

    @Override
    public List<Disease> getItemsForList() {
        return diseaseService.getItemsForList();
    }

    @Override
    public List<Disease> getRecordsByGroup(int groupId) {
        return diseaseService.getRecordsByGroup(groupId);
    }

    @Override
    public boolean canSaveTitle(String title, int groupId, int diseaseId) {
        return diseaseService.canSaveTitle(title, groupId, diseaseId);
    }

    @Override
    public boolean canDelete(int id) {
        return diseaseService.canDelete(id);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return diseaseService.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(Disease persistent, ResearchGroup researchGroup) {
        diseaseService.deleteGroupRel(persistent, researchGroup);
    }
}
