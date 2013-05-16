package cz.zcu.kiv.eegdatabase.data.pojo;


import java.io.Serializable;
import java.util.HashSet;
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

	@ManyToOne
	@JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
	
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

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}
	public Set<PersonalLicense> getPersonalLicenses() {
		return personalLicenses;
    }
	
	public void setResearchGroup(ResearchGroup group) {
		this.researchGroup = group;
	}

	public void setPersonalLicenses(Set<PersonalLicense> personalLicenses) {
		this.personalLicenses = personalLicenses;
    }
	
	public ResearchGroup getResearchGroup() {
		return this.researchGroup;
	}

	public Set<ExperimentPackageLicense> getExperimentPackageLicenses() {
		return experimentPackageLicenses;
	}

	public void setExperimentPackageLicenses(Set<ExperimentPackageLicense> experimentPackageLicenses) {
		this.experimentPackageLicenses = experimentPackageLicenses;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Float.floatToIntBits(this.price);
		hash = 97 * hash + (this.title != null ? this.title.hashCode() : 0);
		hash = 97 * hash + (this.description != null ? this.description.hashCode() : 0);
		hash = 97 * hash + (this.licenseType != null ? this.licenseType.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final License other = (License) obj;
		if (Float.floatToIntBits(this.price) != Float.floatToIntBits(other.price)) {
			return false;
		}
		if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
			return false;
		}
		if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
			return false;
		}
		if (this.licenseType != other.licenseType) {
			return false;
		}
		return true;
	}

	
}
