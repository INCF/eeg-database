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
 *   DigitizationServiceImpl.java, 2015/03/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.DigitizationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Honza on 1.3.15.
 */
public class DigitizationServiceImpl implements DigitizationService {

    private DigitizationDao digitizationDao;

    @Required
    public void setDigitizationDao(DigitizationDao digitizationDao) {
        this.digitizationDao = digitizationDao;
    }

    @Override
    @Transactional
    public void createGroupRel(Digitization persistent, ResearchGroup researchGroup) {
        digitizationDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional
    public List<Digitization> getItemsForList() {
        return digitizationDao.getItemsForList();
    }

    @Override
    @Transactional
    public List<Digitization> getRecordsByGroup(int groupId) {
        return digitizationDao.getRecordsByGroup(groupId);
    }


    @Override
    @Transactional
    public boolean canDelete(int id) {
        return digitizationDao.canDelete(id);
    }

    @Override
    @Transactional
    public boolean hasGroupRel(int id) {
        return digitizationDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(Digitization persistent, ResearchGroup researchGroup) {
        digitizationDao.deleteGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional
    public Integer create(Digitization newInstance) {
        return digitizationDao.create(newInstance);
    }

    @Override
    @Transactional
    public Digitization read(Integer id) {
        return digitizationDao.read(id);
    }

    @Override
    @Transactional
    public List<Digitization> readByParameter(String parameterName, Object parameterValue) {
        return digitizationDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Digitization transientObject) {
        digitizationDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Digitization persistentObject) {
        digitizationDao.delete(persistentObject);
    }

    @Override
    @Transactional
    public List<Digitization> getAllRecords() {
        return digitizationDao.getAllRecords();
    }

    @Override
    @Transactional
    public List<Digitization> getRecordsAtSides(int first, int max) {
        return digitizationDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional
    public int getCountRecords() {
        return digitizationDao.getCountRecords();
    }

    @Override
    @Transactional
    public List<Digitization> getUnique(Digitization example) {
        return digitizationDao.findByExample(example);
    }
}
