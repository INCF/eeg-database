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
 *   ExperimentsOptParamServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments.param;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ExperimentsOptParamServiceImpl implements ExperimentsOptParamService {

    protected Log log = LogFactory.getLog(getClass());

    private ExperimentOptParamDefDao dao;

    private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;

    @Required
    public void setDao(ExperimentOptParamDefDao dao) {
        this.dao = dao;
    }

    @Required
    public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
        this.experimentOptParamValDao = experimentOptParamValDao;
    }

    @Override
    @Transactional
    public Integer create(ExperimentOptParamDef newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentOptParamDef read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(ExperimentOptParamDef transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ExperimentOptParamDef persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    public List<ExperimentOptParamDef> getUnique(ExperimentOptParamDef example) {
        return dao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getItemsForList() {
        return dao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return dao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getRecordsByGroup(int groupId) {
        return dao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional
    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef) {
        dao.createDefaultRecord(experimentOptParamDef);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamDef> getDefaultRecords() {
        return dao.getDefaultRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return dao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        dao.deleteGroupRel(experimentOptParamDefGroupRel);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId) {
        return dao.getGroupRel(experimentOptParamDefId, researchGroupId);
    }

    @Override
    @Transactional
    public void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        dao.createGroupRel(experimentOptParamDefGroupRel);
    }

    @Override
    @Transactional
    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup) {
        dao.createGroupRel(experimentOptParamDef, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return dao.isDefault(id);
    }

    @Override
    @Transactional
    public ExperimentOptParamValId create(ExperimentOptParamVal newInstance) {
        return experimentOptParamValDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentOptParamVal read(ExperimentOptParamValId id) {
        return experimentOptParamValDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamVal> readValByParameter(String parameterName, Object parameterValue) {
        return experimentOptParamValDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(ExperimentOptParamVal transientObject) {
        experimentOptParamValDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ExperimentOptParamVal persistentObject) {
        experimentOptParamValDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamVal> getAllValRecords() {
        return experimentOptParamValDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamVal> getValRecordsAtSides(int first, int max) {
        return experimentOptParamValDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentOptParamVal> getUnique(ExperimentOptParamVal example) {
        return experimentOptParamValDao.findByExample(example);
    }

}
