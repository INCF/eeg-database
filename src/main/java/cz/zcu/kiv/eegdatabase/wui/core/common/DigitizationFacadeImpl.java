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
 *   DigitizationFacadeImpl.java, 2015/03/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by Honza on 1.3.15.
 */
public class DigitizationFacadeImpl implements DigitizationFacade {

    DigitizationService digitizationService;

    @Required
    public void setDigitizationService(DigitizationService digitizationService) {
        this.digitizationService = digitizationService;
    }

    @Override
    public void createGroupRel(Digitization persistent, ResearchGroup researchGroup) {

    }

    @Override
    public List<Digitization> getItemsForList() {
        return digitizationService.getItemsForList();
    }

    @Override
    public List<Digitization> getRecordsByGroup(int groupId) {
        return digitizationService.getRecordsByGroup(groupId);
    }

    @Override
    public boolean canDelete(int id) {
        return digitizationService.canDelete(id);
    }

    @Override
    public boolean hasGroupRel(int id) {
        return digitizationService.hasGroupRel(id);
    }

    @Override
    public void deleteGroupRel(Digitization persistent, ResearchGroup researchGroup) {
        digitizationService.deleteGroupRel(persistent, researchGroup);
    }

    @Override
    public Integer create(Digitization newInstance) {
        return digitizationService.create(newInstance);
    }

    @Override
    public Digitization read(Integer id) {
        return digitizationService.read(id);
    }

    @Override
    public List<Digitization> readByParameter(String parameterName, Object parameterValue) {
        return digitizationService.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Digitization transientObject) {
        digitizationService.update(transientObject);
    }

    @Override
    public void delete(Digitization persistentObject) {
        digitizationService.delete(persistentObject);
    }

    @Override
    public List<Digitization> getAllRecords() {
        return digitizationService.getAllRecords();
    }

    @Override
    public List<Digitization> getRecordsAtSides(int first, int max) {
        return digitizationService.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return digitizationService.getCountRecords();
    }

    @Override
    public List<Digitization> getUnique(Digitization example) {
        return digitizationService.getUnique(example);
    }
}
