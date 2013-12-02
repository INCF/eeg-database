package cz.zcu.kiv.eegdatabase.data.pojo;

// Generated 2.12.2013 0:56:28 by Hibernate Tools 3.4.0.CR1
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters.IAutoCompletable;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

/**
 * Pharmaceutical generated by hbm2java
 */
@Entity
@Table(name = "PHARMACEUTICAL")
public class Pharmaceutical implements java.io.Serializable, IAutoCompletable {

	private int pharmaceuticalId;
	private String title;
	private String description;
	private Set<Experiment> experiments = new HashSet<Experiment>(0);
	private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

	public Pharmaceutical() {
	}

	public Pharmaceutical(String title, String description,
					Set<Experiment> experiments, Set<ResearchGroup> researchGroups) {
		this.title = title;
		this.description = description;
		this.experiments = experiments;
		this.researchGroups = researchGroups;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "PHARMACEUTICAL_ID", nullable = false, precision = 22, scale = 0)
	public int getPharmaceuticalId() {
		return this.pharmaceuticalId;
	}

	public void setPharmaceuticalId(int pharmaceuticalId) {
		this.pharmaceuticalId = pharmaceuticalId;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PHARMACEUTICAL_REL", joinColumns = {
		@JoinColumn(name = "PHARMACEUTICAL_ID", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "EXPERIMENT_ID", nullable = false, updatable = false)})
	public Set<Experiment> getExperiments() {
		return this.experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PHARMACEUTICAL_GROUP_REL", joinColumns = {
		@JoinColumn(name = "PHARMACEUTICAL_ID", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "RESEARCH_GROUP_ID", nullable = false, updatable = false)})
	public Set<ResearchGroup> getResearchGroups() {
		return this.researchGroups;
	}

	public void setResearchGroups(Set<ResearchGroup> researchGroups) {
		this.researchGroups = researchGroups;
	}

	@Override
	@Transient
	public String getAutoCompleteData() {
		return getTitle();
	}
}
