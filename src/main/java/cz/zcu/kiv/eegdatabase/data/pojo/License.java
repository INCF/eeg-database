/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   License.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;


import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *
 * @author bydga
 */
@Entity
@Table(name="LICENSE")
public class License implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LICENSE_ID")
    private int licenseId;
	
    @Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;

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

	@Column(name = "IS_TEMPLATE")
	private boolean template;
	
	@Column(name = "ATTACHMENT_FILE_NAME")
    private String attachmentFileName;
    
    @Lob
    @Column(name = "ATTACHMENT_CONTENT")
    private Blob attachmentContent;
    
    @Transient
    private InputStream fileContentStream;

	public int getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(int licenseId) {
		this.licenseId = licenseId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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
	
	public String getAttachmentFileName() {
        return attachmentFileName;
    }
	
	public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }
	
	public Blob getAttachmentContent() {
        return attachmentContent;
    }
	
	public void setAttachmentContent(Blob attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public void copyFromTemplate(License template) {
		this.description = template.description;
		this.title = template.title;
		this.price = template.price;
		this.licenseType = template.licenseType;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.price != null ? this.price.hashCode() : 0);
		hash = 79 * hash + (this.title != null ? this.title.hashCode() : 0);
		hash = 79 * hash + (this.description != null ? this.description.hashCode() : 0);
		hash = 79 * hash + (this.licenseType != null ? this.licenseType.hashCode() : 0);
		hash = 79 * hash + (this.template ? 1 : 0);
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
		if (this.price != other.price && (this.price == null || !this.price.equals(other.price))) {
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
		if (this.template != other.template) {
			return false;
		}
		return true;
	}
	
	@Transient
	public String getLicenseInfo(){
	    return title + " price: " + (price == null || price.compareTo(BigDecimal.ZERO) == 0 ? "No" : price);
	}
	
	public void setFileContentStream(InputStream inputStream) {
        this.fileContentStream = inputStream;
    }
    
    @Transient
    public InputStream getFileContentStream() {
        return fileContentStream;
    }
		
}
