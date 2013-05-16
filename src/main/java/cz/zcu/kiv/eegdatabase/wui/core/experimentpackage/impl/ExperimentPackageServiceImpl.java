package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageLicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.license.impl.InvalidLicenseForPackageException;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageServiceImpl extends GenericServiceImpl<ExperimentPackage, Integer> implements ExperimentPackageService {

    private ExperimentPackageDao experimentPackageDao;
    private ExperimentPackageLicenseDao experimentPackageLicenseDao;
    private ExperimentPackageConnectionDao experimentPackageConnectionDao;
	
    private LicenseService licenseService;

    public ExperimentPackageServiceImpl() {
    }

    public ExperimentPackageServiceImpl(GenericDao<ExperimentPackage, Integer> dao) {
	super(dao);
    }

	@Required
	public void setExperimentPackageDao(ExperimentPackageDao experimentPackageDao) {
		this.experimentPackageDao = experimentPackageDao;
	}
	
	@Required
	public void setExperimentPackageLicenseDao(ExperimentPackageLicenseDao experimentPackageLicenseDao) {
		this.experimentPackageLicenseDao = experimentPackageLicenseDao;
	}
	
	@Required
	public void setExperimentPackageConnectionDao(ExperimentPackageConnectionDao experimentPackageConnectionDao) {
		this.experimentPackageConnectionDao = experimentPackageConnectionDao;
	}

	@Required
	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId) {
	return experimentPackageDao.readByParameter("researchGroup.researchGroupId", researchGroupId);
    }

	@Override
	@Transactional
	public Integer create(ExperimentPackage pack, License license) {
		int id = this.create(pack);
		pack.setExperimentPackageId(id);
		
		//create default owner license
		License ownerLicense = this.licenseService.getOwnerLicense(pack.getResearchGroup());
		licenseService.addLicenseForPackage(ownerLicense, pack);
		
		if (license != null) {
			licenseService.addLicenseForPackage(license, pack);
		}
		else if (!pack.getResearchGroup().isPaidAccount()) { //specifying that only private will be available but group doesn't have rights to do this
			throw new InvalidLicenseForPackageException("Group " + pack.getResearchGroup().getTitle() + " cannot have private packages");
		}
		
		return id;
	}

	@Override
	@Transactional
	public void delete(ExperimentPackage persistentObject) {
		this.experimentPackageConnectionDao.removeAllConnections(persistentObject);
		this.experimentPackageLicenseDao.removeAllConnections(persistentObject);
		
		super.delete(persistentObject); 
	}

	@Override
	@Transactional
	public List<ExperimentPackage> listVisiblePackages(Person person) {
		return this.experimentPackageDao.listVisiblePackages(person);
	}
	
}
