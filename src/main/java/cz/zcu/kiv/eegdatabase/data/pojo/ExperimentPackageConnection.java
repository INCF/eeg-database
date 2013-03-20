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
    @Column(name = "EXPERIMENT_PACKAGE_CONNECTION_ID")
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
