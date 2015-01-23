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
 *   PersonalLicenseServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonalLicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicenseState;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupService;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;

/**
 *
 * @author J. Danek
 */
public class PersonalLicenseServiceImpl extends GenericServiceImpl<PersonalLicense, Integer> implements PersonalLicenseService {
    
    protected Log log = LogFactory.getLog(getClass());
    
	private PersonalLicenseDao personalLicenseDao;
	private MailService mailService;
	private ResearchGroupService groupService;
	
    public PersonalLicenseServiceImpl() {
    }

    public PersonalLicenseServiceImpl(GenericDao<PersonalLicense, Integer> dao) {
	super(dao);
    }
	
	@Required
    public void setPersonalLicenseDao(PersonalLicenseDao personalLicenseDao) {
		this.personalLicenseDao = personalLicenseDao;
    }
	
	@Required
    public void setMailService(MailService mailService) {
		this.mailService = mailService;
    }
	
	@Required
	public void setGroupFacade(ResearchGroupService groupService) {
        this.groupService = groupService;
    }

	@Override
	@Transactional
	public boolean addLicenseToPerson(Person person, License licence) {
		PersonalLicense personalLicense = new PersonalLicense();
		personalLicense.setLicense(licence);
		personalLicense.setPerson(person);
		personalLicense.setRequestedDate(new Date());
		int id = personalLicenseDao.create(personalLicense);
		
		return id > 0;
	}

	@Override
	@Transactional
	public void createRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setConfirmedDate(null);
		personalLicense.setRequestedDate(new Date());
		personalLicense.setLicenseState(PersonalLicenseState.APPLICATION);
		this.personalLicenseDao.create(personalLicense);
		
		ResearchGroup group = groupService.getResearchGroupById(personalLicense.getLicense().getResearchGroup().getResearchGroupId());
		
		this.mailService.sendLicenseRequestToApplicantEmail(personalLicense.getEmail(), personalLicense.getLicense().getTitle());
		this.mailService.sendLicenseRequestToGroupEmail(
				group.getPerson().getUsername(),
				personalLicense.getFirstName() + " " + personalLicense.getLastName(),
				personalLicense.getEmail(),
				personalLicense.getLicense().getTitle());
	}
	
    @Override
    @Transactional
    public Integer create(PersonalLicense newInstance) {

        try {

            InputStream fileContentStream = newInstance.getFileContentStream();
            if (fileContentStream != null) {
                Blob createBlob = personalLicenseDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob(fileContentStream, fileContentStream.available());
                newInstance.setAttachmentContent(createBlob);
            }

            return personalLicenseDao.create(newInstance);

        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

	@Override
	@Transactional
	public void confirmRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setConfirmedDate(new Date());
		personalLicense.setLicenseState(PersonalLicenseState.AUTHORIZED);
		this.personalLicenseDao.merge(personalLicense);	
		this.mailService.sendLicenseRequestConfirmationEmail(personalLicense.getEmail(), personalLicense.getLicense().getTitle());
	}

	@Override
	@Transactional(readOnly=true)
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group, PersonalLicenseState state) {
		return personalLicenseDao.getLicenseRequests(group, state);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PersonalLicense> getLicenseRequests(Person applicant, PersonalLicenseState state) {
		return personalLicenseDao.getLicenseRequests(applicant, state);
	}

	@Override
	public void rejectRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setLicenseState(PersonalLicenseState.REJECTED);
		personalLicense.setConfirmedDate(new Date());
		this.personalLicenseDao.merge(personalLicense);
		this.mailService.sendLicenseRequestRejectionEmail(personalLicense.getEmail(), personalLicense.getLicense().getTitle(), personalLicense.getResolutionComment());
	}

	@Override
	@Transactional(readOnly=true)
	public List<License> getUsersLicenses(Person person) {
		return this.personalLicenseDao.getUsersLicenses(person);
	}

    @Override
    @Transactional(readOnly=true)
    public byte[] getPersonalLicenseAttachmentContent(int personalLicenseId) {
        return personalLicenseDao.getAttachmentContent(personalLicenseId);
    }
    
    @Override
    @Transactional
    public void update(PersonalLicense transientObject) {

        try {
            // XXX WORKAROUND for Hibernate pre 4.0, update entity with blob this way.
            PersonalLicense merged = personalLicenseDao.merge(transientObject);
            InputStream fileContentStream = transientObject.getFileContentStream();
            if (fileContentStream != null) {
                Blob createBlob;
                createBlob = personalLicenseDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob(fileContentStream, fileContentStream.available());
                merged.setAttachmentContent(createBlob);
                personalLicenseDao.update(merged);
            }
        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
