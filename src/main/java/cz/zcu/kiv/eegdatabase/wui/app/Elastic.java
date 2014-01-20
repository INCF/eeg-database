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
 * DbMigrationPage.java, 2013/10/02 00:01 Jakub Rinkes
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
import cz.zcu.kiv.eegdatabase.data.elasticsearch.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.elasticsearch.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.elasticsearch.entities.ParameterAttribute;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import java.util.List;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
	@SpringBean(name = "artifactDao")
	GenericListDao<Artifact> artifactDao;
	@SpringBean
	DigitizationDao digitizationDao;
	
	@SpringBean
	PersonFacade personFacade;

	public Elastic() {
		
//		reinsert();
		personFacade.changeUserPassword("bydgam@gmail.com", "necum");
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
	
	private void findByEsMethod()
	{
		List<Experiment> findByGenericParameter = experimentDao.findByGenericParameter("temperature", -15);
	
		System.out.println("found2:");
		System.out.println("##########################");
		for (Experiment experiment : findByGenericParameter) {
			System.out.println(experiment.getElasticExperiment().getExperimentId());
			System.out.println(experiment.getExperimentId());
			for (GenericParameter p : experiment.getGenericParameters()) {

				System.out.println(p.getName() + " : " + p.getValueString());
			}
		}
		
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

	private void reinsert() {

		List<Experiment> exps = experimentDao.getAllRecordsFull();
		System.out.println("doing " + exps.size());
		for (Experiment e : exps) {
			experimentDao.update(e);
		}


	}
}
