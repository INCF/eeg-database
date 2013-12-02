package cz.zcu.kiv.eegdatabase.data.pojo;

// Generated 2.12.2013 0:56:28 by Hibernate Tools 3.4.0.CR1

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * Reservation generated by hbm2java
 */
@Entity
@Table(name = "RESERVATION")
public class Reservation implements java.io.Serializable {

	private int reservationId;
	private Person person;
	private Timestamp creationTime;
	private ResearchGroup researchGroup;
	private Timestamp startTime;
	private Timestamp endTime;

	public Reservation() {
	}

	public Reservation(Person person, ResearchGroup researchGroup) {
		this.person = person;
		this.researchGroup = researchGroup;
	}

	public Reservation(Person person, Timestamp creationTime,
			ResearchGroup researchGroup, Timestamp startTime, Timestamp endTime) {
		this.person = person;
		this.creationTime = creationTime;
		this.researchGroup = researchGroup;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "RESERVATION_ID", nullable = false, precision = 22, scale = 0)
	public int getReservationId() {
		return this.reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "CREATION_TIME", length = 7)
	public Timestamp getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESEARCH_GROUP_ID", nullable = false)
	public ResearchGroup getResearchGroup() {
		return this.researchGroup;
	}

	public void setResearchGroup(ResearchGroup researchGroup) {
		this.researchGroup = researchGroup;
	}

	@Column(name = "START_TIME", length = 7)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 7)
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

}
