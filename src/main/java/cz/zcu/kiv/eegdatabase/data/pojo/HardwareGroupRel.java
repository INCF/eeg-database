package cz.zcu.kiv.eegdatabase.data.pojo;

// Generated 2.12.2013 0:56:28 by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * HardwareGroupRel generated by hbm2java
 */
@Entity
@Table(name = "HARDWARE_GROUP_REL")
public class HardwareGroupRel implements java.io.Serializable {

	private HardwareGroupRelId id;
	private ResearchGroup researchGroup;
	private Hardware hardware;

	public HardwareGroupRel() {
	}

	public HardwareGroupRel(HardwareGroupRelId id, ResearchGroup researchGroup,
			Hardware hardware) {
		this.id = id;
		this.researchGroup = researchGroup;
		this.hardware = hardware;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "hardwareId", column = @Column(name = "HARDWARE_ID", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "researchGroupId", column = @Column(name = "RESEARCH_GROUP_ID", nullable = false, precision = 22, scale = 0)) })
	public HardwareGroupRelId getId() {
		return this.id;
	}

	public void setId(HardwareGroupRelId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESEARCH_GROUP_ID", nullable = false, insertable = false, updatable = false)
	public ResearchGroup getResearchGroup() {
		return this.researchGroup;
	}

	public void setResearchGroup(ResearchGroup researchGroup) {
		this.researchGroup = researchGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HARDWARE_ID", nullable = false, insertable = false, updatable = false)
	public Hardware getHardware() {
		return this.hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

}
