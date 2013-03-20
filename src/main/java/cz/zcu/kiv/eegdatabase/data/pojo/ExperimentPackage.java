package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.Set;
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
@Table(name = "EXPERIMENT_PACKAGE")
public class ExperimentPackage implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EXPERIMENT_PACKAGE_ID")
	private int experimentPackageId;
	
	@ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP")
	private ResearchGroup researchGroup;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToMany(mappedBy= "EXPERIMENT_PACKAGE_LICENSES")
	private Set<ExperimentPackageLicense> experimentPackageLicenses;
	
	@OneToMany(mappedBy = "EXPERIMENT_PACKAGE_CONNECTIONS")
	private Set<ExperimentPackageConnection> experimentPackageConnections;

	public int getExperimentPackageId() {
		return experimentPackageId;
	}

	public void setExperimentPackageId(int experimentPackageId) {
		this.experimentPackageId = experimentPackageId;
	}

	public ResearchGroup getResearchGroup() {
		return researchGroup;
	}

	public void setResearchGroup(ResearchGroup researchGroup) {
		this.researchGroup = researchGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ExperimentPackageLicense> getExperimentPackageLicenses() {
		return experimentPackageLicenses;
	}

	public void setExperimentPackageLicenses(Set<ExperimentPackageLicense> experimentPackageLicenses) {
		this.experimentPackageLicenses = experimentPackageLicenses;
	}

	public Set<ExperimentPackageConnection> getExperimentPackageConnections() {
		return experimentPackageConnections;
	}

	public void setExperimentPackageConnections(Set<ExperimentPackageConnection> experimentPackageConnections) {
		this.experimentPackageConnections = experimentPackageConnections;
	}
	
	
}
