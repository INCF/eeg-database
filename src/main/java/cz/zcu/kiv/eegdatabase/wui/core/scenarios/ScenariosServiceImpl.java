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
 *   ScenariosServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioSchemasDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

public class ScenariosServiceImpl implements ScenariosService {

    protected Log log = LogFactory.getLog(getClass());

    ScenarioDao scenarioDAO;
    ScenarioSchemasDao schemaDAO;

    @Required
    public void setSchemaDAO(ScenarioSchemasDao schemaDAO) {
        this.schemaDAO = schemaDAO;
    }

    @Required
    public void setScenarioDAO(ScenarioDao scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }

    @Override
    @Transactional
    public Integer create(Scenario newInstance) {
        return scenarioDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Scenario read(Integer id) {
        return scenarioDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> readByParameter(String parameterName, Object parameterValue) {
        return scenarioDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Scenario transientObject) {
        scenarioDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Scenario persistentObject) {
        scenarioDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getAllRecords() {
        return scenarioDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getRecordsAtSides(int first, int max) {
        return scenarioDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return scenarioDAO.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getUnique(Scenario example) {
        return scenarioDAO.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosWhereOwner(Person owner) {
        return scenarioDAO.getScenariosWhereOwner(owner);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosWhereOwner(Person person, int LIMIT) {
        return scenarioDAO.getScenariosWhereOwner(person, LIMIT);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId) {
        return scenarioDAO.getScenarioSearchResults(request, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int id) {
        return scenarioDAO.canSaveTitle(title, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosForList(Person person, int start, int count) {
        return scenarioDAO.getScenariosForList(person, start, count);
    }

    @Override
    @Transactional(readOnly = true)
    public int getScenarioCountForList(Person person) {
        return scenarioDAO.getScenarioCountForList(person);
    }

    @Override
    public void flush() {
        scenarioDAO.flush();
    }
    
    @Override
    @Transactional
    public Integer createScenarioSchema(ScenarioSchemas newInstance) {
        return schemaDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Scenario getScenarioByTitle(String title) {
        List<Scenario> scenarios = scenarioDAO.readByParameter("title", title);
        if(scenarios.size() > 0){
            return scenarios.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioSchemas readScenarioSchema(Integer id) {
        return schemaDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, int parameterValue) {
        return schemaDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, String parameterValue) {
        return schemaDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void updateScenarioSchema(ScenarioSchemas transientObject) {
        schemaDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void deleteScenarioSchema(ScenarioSchemas persistentObject) {
        schemaDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioSchemas> getAllScenarioSchemasRecords() {
        return schemaDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioSchemas> getScenarioSchemasRecordsAtSides(int first, int max) {
        return schemaDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountScenarioSchemasRecords() {
        return schemaDAO.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public int getNextSchemaId() {
        return schemaDAO.getNextSchemaId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioSchemas> getListOfScenarioSchemas() {
        return schemaDAO.getListOfScenarioSchemas();
    }

}
