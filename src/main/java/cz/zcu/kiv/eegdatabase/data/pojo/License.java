package cz.zcu.kiv.eegdatabase.data.pojo;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
/**
 *
 * @author bydga
 */
@Entity
@Table(name="LICENSE")
public class License implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LICENSE_ID")
    private int licenseId;
	
    @Column(name = "PRICE")
    private float price;
	
    @Column(name = "TITLE")
    private String title;
	
    @Column(name = "DESCRIPTION")
    private String description;
	
	@OneToMany(mappedBy = "license")
	private Set<PersonalLicense> personalLicenses;
	
	@OneToMany(mappedBy = "license")
	private Set<ExperimentPackageLicense> experimentPackageLicenses;
	
	@Column(name = "LICENSE_TYPE" )
	private LicenseType licenseType;

	public int getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(int licenseId) {
		this.licenseId = licenseId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<PersonalLicense> getPersonalLicenses() {
		return personalLicenses;
	}

	public void setPersonalLicenses(Set<PersonalLicense> personalLicenses) {
		this.personalLicenses = personalLicenses;
	}

	public Set<ExperimentPackageLicense> getExperimentPackageLicenses() {
		return experimentPackageLicenses;
	}

	public void setExperimentPackageLicenses(Set<ExperimentPackageLicense> experimentPackageLicenses) {
		this.experimentPackageLicenses = experimentPackageLicenses;
	}

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}
	

}
