package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
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
		else if (!pack.getResearchGroup().isPaidAccount()) {
			throw new InvalidLicenseForPackageException("Group " + pack.getResearchGroup().getTitle() + " cannot have private packages");
		}
		
		return id;
	}

}
