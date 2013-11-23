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
 *   ScenariosFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

public class ScenariosFacadeImpl implements ScenariosFacade {

    protected Log log = LogFactory.getLog(getClass());

    ScenariosService service;

    @Required
    public void setService(ScenariosService service) {
        this.service = service;
    }

    @Override
    public Integer create(Scenario newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Scenario read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Scenario> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Scenario transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Scenario persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Scenario> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Scenario> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Scenario> getUnique(Scenario example) {
        return service.getUnique(example);
    }

    @Override
    public List<Scenario> getScenariosWhereOwner(Person owner) {
        return service.getScenariosWhereOwner(owner);
    }


    @Override
    public List<Scenario> getScenariosWhereOwner(Person person, int LIMIT) {
        return service.getScenariosWhereOwner(person, LIMIT);
    }

    @Override
    public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId) {
        return service.getScenarioSearchResults(request, personId);
    }

    @Override
    public boolean canSaveTitle(String title, int id) {
        return service.canSaveTitle(title, id);
    }

    @Override
    public List<Scenario> getScenariosForList(Person person, int start, int count) {
        return service.getScenariosForList(person, start, count);
    }

    @Override
    public int getScenarioCountForList(Person person) {
        return service.getScenarioCountForList(person);
    }
    
    @Override
    public boolean existsScenario(String title) {
        List<Scenario> scenarios = service.readByParameter("title",title);
        return scenarios != null && scenarios.size() > 0;
    }

    @Override
    public void flush() {
        service.flush();
    }

    @Override
    public Scenario getScenarioByTitle(String title) {
        return service.getScenarioByTitle(title);
    }   
    
//    @Override
//    public Integer create(ScenarioSchemas newInstance) {
//        return service.create(newInstance);
//    }

    @Override
    public Integer createScenarioSchema(ScenarioSchemas newInstance) {
        return service.createScenarioSchema(newInstance);
    }

    @Override
    public ScenarioSchemas readScenarioSchema(Integer id) {
        return service.readScenarioSchema(id);
    }

    @Override
    public List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, int parameterValue) {
        return service.readScenarioSchemaByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, String parameterValue) {
        return service.readScenarioSchemaByParameter(parameterName, parameterValue);
    }

    @Override
    public void updateScenarioSchema(ScenarioSchemas transientObject) {
        service.updateScenarioSchema(transientObject);
    }

    @Override
    public void deleteScenarioSchema(ScenarioSchemas persistentObject) {
        service.deleteScenarioSchema(persistentObject);
    }

    @Override
    public List<ScenarioSchemas> getAllScenarioSchemasRecords() {
        return service.getAllScenarioSchemasRecords();
    }

    @Override
    public List<ScenarioSchemas> getScenarioSchemasRecordsAtSides(int first, int max) {
        return service.getScenarioSchemasRecordsAtSides(first, max);
    }

    @Override
    public int getCountScenarioSchemasRecords() {
        return service.getCountScenarioSchemasRecords();
    }

    @Override
    public int getNextSchemaId() {
        return service.getNextSchemaId();
    }

    @Override
    public List<ScenarioSchemas> getListOfScenarioSchemas() {
        return service.getListOfScenarioSchemas();
    }
}
