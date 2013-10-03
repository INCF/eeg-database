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
 *   SimpleExperimentPackageLicenseDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import org.hibernate.Session;

/**
 *
 * @author bydga
 */
public class SimpleExperimentPackageLicenseDao extends SimpleGenericDao<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseDao {

    public SimpleExperimentPackageLicenseDao() {
	super(ExperimentPackageLicense.class);
    }
	
	
	@Override
	public void removeLicenseFromPackage(int packageId, int licenseId )
	{
		String hqlQuery = "delete from ExperimentPackageLicense epl where epl.experimentPackage = :ep and epl.license = :l";
        Session session = getSession();
        session.createQuery(hqlQuery).setInteger("ep", packageId).setInteger("l", licenseId).executeUpdate();
	}

	@Override
	public void removeAllConnections(ExperimentPackage pack) {
		String hqlQuery = "delete from ExperimentPackageLicense epl where epl.experimentPackage = :ep";
        this.getSession().createQuery(hqlQuery).setInteger("ep", pack.getExperimentPackageId()).executeUpdate();
	}
}
