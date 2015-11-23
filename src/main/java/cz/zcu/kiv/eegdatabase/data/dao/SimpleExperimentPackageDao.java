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
 *   SimpleExperimentPackageDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author bydga
 */
public class SimpleExperimentPackageDao extends SimpleGenericDao<ExperimentPackage, Integer> implements ExperimentPackageDao {

	public SimpleExperimentPackageDao() {
		super(ExperimentPackage.class);
	}

	@Override
	public List<ExperimentPackage> listVisiblePackages(Person person) {
		String HQL = "SELECT DISTINCT ep FROM ExperimentPackage ep, ExperimentPackageConnection epc, ExperimentPackageLicense epl, PersonalLicense pl"
				+ " WHERE ep.experimentPackageId = epl.experimentPackage.experimentPackageId"
		//		+ " AND ( (epl.license.licenseType != :licenseType)"
		//		+ " OR (epl.license.licenseId = pl.license.licenseId"
		//		+ " AND pl.person.personId = :personId))"
				+ " AND ep.experimentPackageId = epc.experimentPackage.experimentPackageId";

		Query query = getSessionFactory().getCurrentSession().createQuery(HQL);
		//query.setParameter("licenseType", LicenseType.OWNER);
		//query.setParameter("personId", person.getPersonId());
		return query.list();
	}
}
