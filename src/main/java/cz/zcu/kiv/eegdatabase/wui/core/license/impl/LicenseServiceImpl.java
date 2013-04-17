/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageLicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author veveri
 */
public class LicenseServiceImpl extends GenericServiceImpl<License, Integer> implements LicenseService {

	private ExperimentPackageLicenseDao experimentPackageLicenseDao;
	private LicenseDao licenseDao;

	public LicenseServiceImpl() {
	}

	public void setExperimentPackageLicenseDao(ExperimentPackageLicenseDao experimentPackageLicenseDao) {
		this.experimentPackageLicenseDao = experimentPackageLicenseDao;
	}

	public LicenseServiceImpl(GenericDao<License, Integer> dao) {
		super(dao);
	}

	@Override
	@Transactional
	public void addLicenseForPackage(License license, ExperimentPackage pack) {

		int res = this.licenseDao.create(license);
		license.setLicenseId(res);
		ExperimentPackageLicense conn = new ExperimentPackageLicense();
		conn.setExperimentPackage(pack);
		conn.setLicense(license);
		this.experimentPackageLicenseDao.create(conn);
	}

	@Override
	@Transactional
	public void removeLicenseFromPackage(License license, ExperimentPackage pack) {
		this.experimentPackageLicenseDao.removeLicenseFromPackage(pack.getExperimentPackageId(), license.getLicenseId());
	}

	@Override
	public License getPublicLicense() {
		return this.licenseDao.getPublicLicense();
	}
}
