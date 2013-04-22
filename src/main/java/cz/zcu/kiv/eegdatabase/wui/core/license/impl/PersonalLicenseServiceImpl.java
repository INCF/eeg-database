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
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author veveri
 */
public class PersonalLicenseServiceImpl extends GenericServiceImpl<PersonalLicense, Integer> implements PersonalLicenseService {

	public PersonalLicenseServiceImpl() {
	}
	PersonalLicenseDao personalLicenseDao;

	@Required
	public void setPersonalLicenseDao(PersonalLicenseDao personalLicenseDao) {
		this.personalLicenseDao = personalLicenseDao;
	}

	public PersonalLicenseServiceImpl(GenericDao<PersonalLicense, Integer> dao) {
		super(dao);
	}

	@Override
	@Transactional
	public void addLicenseToPerson(Person p, License license) {
		PersonalLicense personalLicense = new PersonalLicense();
		personalLicense.setLicense(license);
		personalLicense.setPerson(p);
		personalLicense.setRequestedDate(new Date());
		this.personalLicenseDao.create(personalLicense);
	}

	@Override
	@Transactional
	public void createRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setConfirmedDate(null);
		this.personalLicenseDao.create(personalLicense);
	}

	@Override
	@Transactional
	public void confirmRequestForLicense(PersonalLicense personalLicense) {
		personalLicense.setConfirmedDate(new Date());
		this.personalLicenseDao.update(personalLicense);
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
