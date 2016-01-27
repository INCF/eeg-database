/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * ScenarioDao.java, 2013/10/02 00:01 Jakub Rinkes
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import java.sql.Blob;
import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.search.SearchRequest;

/**
 * DAO for fetching and saving objects with people.
 *
 * @author Jindrich Pergler
 */
public interface ScenarioDao extends GenericDao<Scenario, Integer> {

	public List<Scenario> getScenariosWhereOwner(Person owner);

	public List<Scenario> getScenariosWhereOwner(Person person, int LIMIT);

	public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

	boolean canSaveTitle(String title, int id);

	public List<Scenario> getScenariosForList(Person person, int start, int count);

	/**
	 * Getter of scenarios which belong to user's research group, or are public.
	 *
	 * @param person user
	 * @return list of scenarios
	 */
	public List<Scenario> getAvailableScenarios(Person person);

	public int getScenarioCountForList(Person person);

	public void flush();

	public Blob getScenarioFile(int scenarioId);
}
