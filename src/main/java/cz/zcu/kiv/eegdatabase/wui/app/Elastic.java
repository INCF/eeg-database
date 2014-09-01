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
 * Elastic.java, 2013/10/02 00:01 Jakub Rinkes
 * ****************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.app;

import cz.zcu.kiv.eegdatabase.data.dao.DigitizationDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericListDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.ParameterAttribute;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import java.util.List;
import javax.annotation.Resource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data migration logic.
 *
 * Thread.sleep is used to avoid running out of connections. Not pretty, yet
 * suffitient for the time being.
 *
 * @author J. Danek
 */
public class Elastic extends BasePage {

	@SpringBean
	ExperimentDao experimentDao;
	@SpringBean
	PersonDao personDao;
	@SpringBean
	DigitizationDao digitizationDao;
	@SpringBean
	PersonFacade personFacade;
	@Resource
	ElasticsearchTemplate elasticsearchTemplate;

	public Elastic() {
		super();
		System.out.println("IMHERE");
		//test();
        reinsert();

    }

	@Transactional
	void test() {
		Experiment read = this.experimentDao.read(245);
		Experiment newExperiment = new Experiment(read.getWeather(), read.getPersonBySubjectPersonId(), read.getScenario(), read.getPersonByOwnerId(), read.getResearchGroup(), read.getDigitization(), read.getSubjectGroup(), read.getArtifact(), read.getElectrodeConf());
		newExperiment.getGenericParameters().add(new GenericParameter("test1", "value1"));
		newExperiment.getGenericParameters().add(new GenericParameter("test2", "value2"));
		int newId = experimentDao.create(newExperiment);

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + newId)).build();
		List<ExperimentElastic> elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
		if (elastic.size() != 1) {
			throw new RuntimeException("Missing doc in Elastic");
		}

		ExperimentElastic exp = elastic.get(0);
		if (exp.getParams().get(0).getName().compareTo("test1") != 0) {
			throw new RuntimeException("Invalid param test1");
		}
		if (exp.getParams().get(0).getValueString().compareTo("value1") != 0) {
			throw new RuntimeException("Invalid param value value1");
		}

		if (exp.getParams().get(1).getName().compareTo("test2") != 0) {
			throw new RuntimeException("Invalid param test2");
		}
		if (exp.getParams().get(1).getValueString().compareTo("value2") != 0) {
			throw new RuntimeException("Invalid param value value2");
		}

		experimentDao.delete(newExperiment);

		searchQuery = new NativeSearchQueryBuilder().withQuery(new IdsQueryBuilder("experiment").addIds("" + newId)).build();
		elastic = elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
		if (elastic.size() != 0) {
			throw new RuntimeException("Elastic doc should be missing and I found one.");
		}
	}

	@Transactional
	void search() {

//		GenericParameter[] yes = {new GenericParameter("hardware", "head CIRCUMFERENCEs"), new GenericParameter("hardware", "visionable"), new GenericParameter("hardware", "blue")};
		GenericParameter[] yes = {new GenericParameter("hardware", "red"), new GenericParameter("software", "blue")};
		GenericParameter[] no = {new GenericParameter("nohardware", "xxx")};
		List<Experiment> searchByParameters = experimentDao.searchByParameters(yes, no);
		System.out.println("got1 " + searchByParameters.size());

//		List<Experiment> search = experimentDao.search("device");
//		System.out.println("got2 " + search.size());
//
//
//		search = experimentDao.searchByParameterRange("temperature", 10, 30);
//		System.out.println("got3 " + search.size());
	}

	private void insertNew() {
		Experiment read = this.experimentDao.read(245);
		Experiment newExperiment = new Experiment(read.getWeather(), read.getPersonBySubjectPersonId(), read.getScenario(), read.getPersonByOwnerId(), read.getResearchGroup(), read.getDigitization(), read.getSubjectGroup(), read.getArtifact(), read.getElectrodeConf());
		experimentDao.create(newExperiment);

	}

	private void delete() {
		Experiment read = this.experimentDao.read(318);
		experimentDao.delete(read);
	}

	@Transactional
	private void findByMethod() {

		Person owner = personDao.read(14);
		List<Experiment> allExperimentsForUser = this.experimentDao.getAllExperimentsForUser(owner, 0, 100);

		System.out.println("found:");
		System.out.println("##########################");
		for (Experiment experiment : allExperimentsForUser) {
			System.out.println(experiment.getElasticExperiment().getExperimentId());
			System.out.println(experiment.getExperimentId());
			for (GenericParameter p : experiment.getGenericParameters()) {

				System.out.println(p.getName() + " : " + p.getValueString());
			}
		}

	}

	private void update() {
		Experiment read = experimentDao.read(245);
		read.getGenericParameters().add(new GenericParameter("updateParam222", "blablabla"));
		experimentDao.update(read);

	}

        @Transactional
	private void reinsert() {

		List<Experiment> exps = experimentDao.getAllRecords();
		System.out.println("doing " + exps.size());
		for (Experiment e : exps) {
//			e = experimentDao.read(e.getExperimentId());
			experimentDao.update(e);
		}


	}
}
