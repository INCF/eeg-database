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
 * ExperimentDao.java, 2013/10/02 00:01 Jakub Rinkes
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * DAO for fetching and saving experiments.
 *
 * @author Jindrich Pergler
 */
public interface ExperimentDao extends GenericDao<Experiment, Integer> {

	public List<Experiment> findByGenericParameter(String name, String value);
	
	public List<Experiment> findByGenericParameter(String name, int value);
	
	public ElasticsearchTemplate getElasticsearchTemplate();

	public List<DataFile> getDataFilesWhereExpId(int experimentId);

	public List<DataFile> getDataFilesWhereId(int dataFileId);

	public Experiment getExperimentForDetail(int experimentId);

	public List<Experiment> getExperimentsWhereOwner(Person person, int limit);

	public List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit);

	int getCountForExperimentsWhereOwner(Person loggedUser);

	public List<Experiment> getExperimentsWhereSubject(Person person, int limit);

	public List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit);

	public int getCountForExperimentsWhereSubject(Person person);

	public int getCountForAllExperimentsForUser(Person person);

	public List<Experiment> getAllExperimentsForUser(Person person, int start, int count);

	public List<Experiment> getRecordsNewerThan(int personId);

	public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId);

	public List<Experiment> getVisibleExperiments(int personId, int start, int limit);

	public int getVisibleExperimentsCount(int personId);
}
