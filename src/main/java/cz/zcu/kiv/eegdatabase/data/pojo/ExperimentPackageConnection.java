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
 *   ExperimentPackageConnection.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author bydga
 */
@Entity
@Table(name = "EXPERIMENT_PACKAGE_CONNECTION")
public class ExperimentPackageConnection implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EPC_ID")
	private int experimentPackgageConnectionId;
	
	
	@ManyToOne
	@JoinColumn(name = "EXPERIMENT")
	private Experiment experiment;
	
	@ManyToOne
	@JoinColumn(name = "EXPERIMENT_PACKAGE")
	private ExperimentPackage experimentPackage;

	public int getExperimentPackgageConnectionId() {
		return experimentPackgageConnectionId;
	}

	public void setExperimentPackgageConnectionId(int experimentPackgageConnectionId) {
		this.experimentPackgageConnectionId = experimentPackgageConnectionId;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public ExperimentPackage getExperimentPackage() {
		return experimentPackage;
	}

	public void setExperimentPackage(ExperimentPackage experimentPackage) {
		this.experimentPackage = experimentPackage;
	}
	
	
}
