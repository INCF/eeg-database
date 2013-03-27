/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author bydga
 */
@Entity
@Table(name="LICENSE")
public class PersonalLicense implements Serializable{
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERSONAL_LICENSE_ID")
    private int personalLicenseId;
	
	
	@ManyToOne
	@JoinColumn(name = "PERSON")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name = "LICENSE")
	private License license;

	@Column(name = "DATE_FROM")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dateFrom;

	public int getPersonalLicenseId() {
		return personalLicenseId;
	}

	public void setPersonalLicenseId(int personalLicenseId) {
		this.personalLicenseId = personalLicenseId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public Date getDateFrom() {
	    return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
	    this.dateFrom = dateFrom;
	}		
}
