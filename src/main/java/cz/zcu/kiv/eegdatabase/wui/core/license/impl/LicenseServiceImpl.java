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
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
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

	@Required
	public void setExperimentPackageLicenseDao(ExperimentPackageLicenseDao experimentPackageLicenseDao) {
		this.experimentPackageLicenseDao = experimentPackageLicenseDao;
	}

	@Required
	public void setLicenseDao(LicenseDao licenseDao) {
		this.licenseDao = licenseDao;
	}

	public LicenseServiceImpl(GenericDao<License, Integer> dao) {
		super(dao);
	}

	@Override
	@Transactional
	public void addLicenseForPackage(License license, ExperimentPackage pack) {
		if(license.getLicenseId() == 0) {
			license.setResearchGroup(pack.getResearchGroup());
			int res = this.licenseDao.create(license);
			license.setLicenseId(res);
		}
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
	@Transactional(readOnly=true)
	public License getPublicLicense() {
		return this.licenseDao.getPublicLicense();
	}

	@Override
	@Transactional(readOnly= true)
	public List<License> getLicensesForGroup(ResearchGroup group, LicenseType type) {
		return this.licenseDao.getLicensesByType(group.getResearchGroupId(), type);
	}

	@Override
	@Transactional(readOnly=true)
	public License getOwnerLicense(ResearchGroup group) {
		return this.getLicensesForGroup(group, LicenseType.OWNER).get(0);
	}

	@Override
	@Transactional(readOnly=true)
	public List<License> getLicensesForGroup(ResearchGroup group, List<LicenseType> type) {
		return this.licenseDao.getLicensesByType(group.getResearchGroupId(), type);
	}

	@Override
	@Transactional(readOnly=true)
	public List<License> getLicensesForPackage(ExperimentPackage pckg) {
		List<ExperimentPackageLicense> connections = experimentPackageLicenseDao.readByParameter("experimentPackage.experimentPackageId", pckg.getExperimentPackageId());

		List<License> ret = new ArrayList<License>(connections.size());
		for(ExperimentPackageLicense epl : connections) {
			ret.add(epl.getLicense());
		}
		
		return ret;
	}

}
