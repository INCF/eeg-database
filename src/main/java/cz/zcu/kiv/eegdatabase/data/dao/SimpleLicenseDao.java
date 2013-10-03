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
 *   SimpleLicenseDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bydga
 */
public class SimpleLicenseDao extends SimpleGenericDao<License, Integer> implements LicenseDao {

    public SimpleLicenseDao() {
	super(License.class);
    }

	@Override
	public List<License> getLicensesByType(int researchGroupId, LicenseType licenseType) {
		List<LicenseType> types = new ArrayList<LicenseType>(1);
		types.add(licenseType);
		return this.getLicensesByType(researchGroupId, types);
	}

	@Override
	public License getPublicLicense() {
		LicenseType licenseType = LicenseType.OPEN_DOMAIN;
		String hqlQuery = "select l from License l where l.licenseType=:licenseType";
		
		return (License) this.getSession().createQuery(hqlQuery).setParameter("licenseType", licenseType).list().iterator().next();
	}

	@Override
	public List<License> getLicensesByType(int reseachGroupId, List<LicenseType> licenseType) {
		String hqlQuery = "select l from License l where l.licenseType IN (:licenseType) and l.researchGroup=:researchGroup";

		return this.getSession().createQuery(hqlQuery).setInteger("researchGroup", reseachGroupId).setParameterList("licenseType", licenseType).list();
	}
    
}
