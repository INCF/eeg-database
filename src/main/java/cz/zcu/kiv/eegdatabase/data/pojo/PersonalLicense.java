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
@Table(name="PERSONAL_LICENSE")
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

    @Column(name = "REQUESTED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requestedDate;
	
	@Column(name = "CONFIRMED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date confirmedDate;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}
	
	public boolean isConfirmed()
	{
		return this.confirmedDate != null;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	@Column(name = "CONFIRMED_DATE")
	private String organization;

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
}
