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
 *   PersonalLicense.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * 
 * @author bydga
 */
@Entity
@Table(name = "PERSONAL_LICENSE")
public class PersonalLicense implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "LICENSE_STATE")
    private PersonalLicenseState licenseState;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "ORGANISATION")
    private String organisation;

    @Column(name = "EMAIL")
    private String email;

    /**
     * Group administrator can provide explanation why the application has been rejected (or any other comment).
     */
    @Column(name = "RESOLUTION_COMMENT")
    private String resolutionComment;

    @Column(name = "ATTACHMENT_FILE_NAME")
    private String attachmentFileName;

    @Lob
    @Column(name = "ATTACHMENT_CONTENT")
    private Blob attachmentContent;
    
    @Transient
    private InputStream fileContentStream;

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

    public PersonalLicenseState getLicenseState() {
        return licenseState;
    }

    public void setLicenseState(PersonalLicenseState licenseState) {
        this.licenseState = licenseState;
    }

    public boolean isConfirmed()
    {
        return this.licenseState == PersonalLicenseState.AUTHORIZED;
    }

    public String getResolutionComment() {
        return resolutionComment;
    }

    public void setResolutionComment(String resolutionComment) {
        this.resolutionComment = resolutionComment;
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

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organization) {
        this.organisation = organization;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        String tmp = this.getFirstName();
        tmp = (tmp == null || tmp.equals("")) ? this.getLastName() : tmp + " " + this.getLastName();
        return tmp;
    }
    
    public void setFileContentStream(InputStream inputStream) {
        this.fileContentStream = inputStream;
    }
    
    @Transient
    public InputStream getFileContentStream() {
        return fileContentStream;
    }
}
