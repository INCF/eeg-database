/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonalLicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author veveri
 */
public class PersonalLicenseServiceImpl extends GenericServiceImpl<PersonalLicense, Integer> implements PersonalLicenseService {

	private PersonalLicenseDao personalLicenseDao;
	private MailService mailService;
	
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
		this.personalLicenseDao.create(personalLicense);
		personalLicense.setConfirmedDate(null);
		this.mailService.sendLicenseRequestToApplicantEmail(personalLicense.getPerson().getEmail(), personalLicense.getLicense().getTitle());
		this.mailService.sendLicenseRequestToGroupEmail(
				personalLicense.getLicense().getResearchGroup().getPerson().getEmail(),
				personalLicense.getFirstName() + " " + personalLicense.getLastName(),
				personalLicense.getPerson().getEmail(),
				personalLicense.getLicense().getTitle());
	}

	@Override
	@Transactional
	public void confirmRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setConfirmedDate(new Date());
		this.personalLicenseDao.update(personalLicense);
		personalLicense.setConfirmedDate(null);		
		this.mailService.sendLicenseRequestConfirmationEmail(personalLicense.getPerson().getEmail(), personalLicense.getLicense().getTitle());
	}

	@Override
	@Transactional(readOnly=true)
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group) {
		return personalLicenseDao.getLicenseRequests(group, false);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted) {
		return personalLicenseDao.getLicenseRequests(applicant, accepted);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PersonalLicense> getGrantedLicenses(ResearchGroup group) {
		return personalLicenseDao.getLicenseRequests(group, true);
	}

	@Override
	public void rejectRequestForLicense(PersonalLicense personalLicense) {
		this.personalLicenseDao.delete(personalLicense);
		this.mailService.sendLicenseRequestRejectionEmail(personalLicense.getPerson().getEmail(), personalLicense.getLicense().getTitle());
	}
}
