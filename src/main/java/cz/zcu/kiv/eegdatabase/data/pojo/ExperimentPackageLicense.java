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
 *   ExperimentPackageLicense.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "EXPERIMENT_PACKAGE_LICENSE")
public class ExperimentPackageLicense implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EXPERIMENT_PACKAGE_LICENSE_ID")
	private int experimentPackageLicenseId;
	
	@ManyToOne
	@JoinColumn(name = "EXPERIMENT_PACKAGE")
	private ExperimentPackage experimentPackage;
	
	@ManyToOne
	@JoinColumn(name = "LICENSE")
	private License license;
	
	@Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;

	
	public int getExperimentPackageLicenseId() {
		return experimentPackageLicenseId;
	}

	public void setExperimentPackageLicenseId(int experimentPackageLicenseId) {
		this.experimentPackageLicenseId = experimentPackageLicenseId;
	}

	public ExperimentPackage getExperimentPackage() {
		return experimentPackage;
	}

	public void setExperimentPackage(ExperimentPackage experimentPackage) {
		this.experimentPackage = experimentPackage;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}
	
	public BigDecimal getPrice() {
        return price;
    }
	
	public void setPrice(BigDecimal price) {
        this.price = price;
    }
	
	
}
