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
 *   PharmaceuticalServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimplePharmaceuticalDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PharmaceuticalServiceImpl implements PharmaceuticalService {

    protected Log log = LogFactory.getLog(getClass());

    SimplePharmaceuticalDao pharmaceuticalDao;

    @Required
    public void setPharmaceuticalDao(SimplePharmaceuticalDao pharmaceuticalDao) {
        this.pharmaceuticalDao = pharmaceuticalDao;
    }

    @Override
    @Transactional
    public Integer create(Pharmaceutical newInstance) {
        return pharmaceuticalDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Pharmaceutical read(Integer id) {
        return pharmaceuticalDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> readByParameter(String parameterName, Object parameterValue) {
        return pharmaceuticalDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Pharmaceutical transientObject) {
        pharmaceuticalDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Pharmaceutical persistentObject) {
        pharmaceuticalDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> getAllRecords() {
        return pharmaceuticalDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> getRecordsAtSides(int first, int max) {
        return pharmaceuticalDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return pharmaceuticalDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> getUnique(Pharmaceutical example) {
        return pharmaceuticalDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int groupId, int pharmaceuticalId) {
        return pharmaceuticalDao.canSaveTitle(title, groupId, pharmaceuticalId);
    }

    @Override
    @Transactional
    public void createGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        pharmaceuticalDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> getItemsForList() {
        return pharmaceuticalDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmaceutical> getRecordsByGroup(int groupId) {
        return pharmaceuticalDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return pharmaceuticalDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return pharmaceuticalDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        pharmaceuticalDao.deleteGroupRel(persistent, researchGroup);
    }
}
