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
import javax.persistence.OneToMany;
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
	
	
	
}
