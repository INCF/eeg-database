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
 *   ExperimentPackageServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageLicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
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
		
		if (license != null) {
			licenseService.addLicenseForPackage(license, pack);
		} else if (!pack.getResearchGroup().isPaidAccount()) { //specifying that only private will be available but group doesn't have rights to do this
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
