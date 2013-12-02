package cz.zcu.kiv.eegdatabase.data.pojo;

// Generated 2.12.2013 0:56:28 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Hardware generated by hbm2java
 */
@Entity
@Table(name = "HARDWARE")
public class Hardware implements java.io.Serializable {

	private int hardwareId;
	private String title;
	private String type;
	private String description;
	private int defaultNumber;
	private Set<Experiment> experiments = new HashSet<Experiment>(0);
	private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

	public Hardware() {
	}

	public Hardware(String title, String type) {
		this.title = title;
		this.type = type;
	}

	public Hardware(String title, String type, String description,
			int defaultNumber, Set<Experiment> experiments,
			Set<ResearchGroup> researchGroups) {
		this.title = title;
		this.type = type;
		this.description = description;
		this.defaultNumber = defaultNumber;
		this.experiments = experiments;
		this.researchGroups = researchGroups;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "HARDWARE_ID", nullable = false, precision = 22, scale = 0)
	public int getHardwareId() {
		return this.hardwareId;
	}

	public void setHardwareId(int hardwareId) {
		this.hardwareId = hardwareId;
	}

	@Column(name = "TITLE", nullable = false, length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "TYPE", nullable = false, length = 30)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "DESCRIPTION", length = 30)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "IS_DEFAULT", precision = 1, scale = 0)
	public int getDefaultNumber() {
		return this.defaultNumber;
	}

	public void setDefaultNumber(int defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "HARDWARE_USAGE_REL", joinColumns = { @JoinColumn(name = "HARDWARE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "EXPERIMENT_ID", nullable = false, updatable = false) })
	public Set<Experiment> getExperiments() {
		return this.experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "HARDWARE_GROUP_REL", joinColumns = { @JoinColumn(name = "HARDWARE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "RESEARCH_GROUP_ID", nullable = false, updatable = false) })
	public Set<ResearchGroup> getResearchGroups() {
		return this.researchGroups;
	}

	public void setResearchGroups(Set<ResearchGroup> researchGroups) {
		this.researchGroups = researchGroups;
	}

}
