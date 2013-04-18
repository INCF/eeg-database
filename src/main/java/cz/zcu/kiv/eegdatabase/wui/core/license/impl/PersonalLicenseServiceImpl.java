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
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

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
	public void addLicenseToPerson(Person p, License license) {
		PersonalLicense personalLicense = new PersonalLicense();
		personalLicense.setLicense(license);
		personalLicense.setPerson(p);
		personalLicense.setDateFrom(new Date());
		this.personalLicenseDao.create(personalLicense);
	}
}
