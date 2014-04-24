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
 *   DiseaseServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleDiseaseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class DiseaseServiceImpl implements DiseaseService {

    SimpleDiseaseDao diseaseDao;

    @Required
    public void setDiseaseDao(SimpleDiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }

    @Override
    @Transactional
    public Integer create(Disease newInstance) {
        return diseaseDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Disease read(Integer id) {
        return diseaseDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> readByParameter(String parameterName, Object parameterValue) {
        return diseaseDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Disease transientObject) {
        diseaseDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Disease persistentObject) {
        diseaseDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> getAllRecords() {
        return diseaseDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> getRecordsAtSides(int first, int max) {
        return diseaseDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return diseaseDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> getUnique(Disease example) {
        return diseaseDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsDisease(String name) {
        List<Disease> existingDisease = diseaseDao.readByParameter("title", name);
        return existingDisease != null && existingDisease.size() > 0;
    }

    @Override
    @Transactional
    public void createGroupRel(Disease persistent, ResearchGroup researchGroup) {
        diseaseDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> getItemsForList() {
        return diseaseDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> getRecordsByGroup(int groupId) {
        return diseaseDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return diseaseDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return diseaseDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(Disease persistent, ResearchGroup researchGroup) {
        diseaseDao.deleteGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int groupId, int diseaseId) {
        return diseaseDao.canSaveTitle(title, groupId, diseaseId);
    }
}
