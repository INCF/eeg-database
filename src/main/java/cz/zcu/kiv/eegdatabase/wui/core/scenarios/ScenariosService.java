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
 *   ScenariosService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ScenariosService extends GenericService<Scenario, Integer> {

    List<Scenario> getScenariosWhereOwner(Person owner);

    List<Scenario> getRecordsNewerThan(long oracleScn);

    List<Scenario> getScenariosWhereOwner(Person person, int LIMIT);

    List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

    boolean canSaveTitle(String title, int id);

    List<Scenario> getScenariosForList(Person person, int start, int count);

    int getScenarioCountForList(Person person);
    
    void flush();

    Scenario getScenarioByTitle(String title);
    
//    Integer create(ScenarioSchemas newInstance);

    Integer createScenarioSchema(ScenarioSchemas newInstance);

    ScenarioSchemas readScenarioSchema(Integer id);

    List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, int parameterValue);

    List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, String parameterValue);

    void updateScenarioSchema(ScenarioSchemas transientObject);

    void deleteScenarioSchema(ScenarioSchemas persistentObject);

    List<ScenarioSchemas> getAllScenarioSchemasRecords();

    List<ScenarioSchemas> getScenarioSchemasRecordsAtSides(int first, int max);

    int getCountScenarioSchemasRecords();
    
    int getNextSchemaId();
    
    List<ScenarioSchemas> getListOfScenarioSchemas();
}
