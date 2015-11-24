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
 *   LicenseService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.util.List;

/**
 *
 * @author J. Danek
 */
public interface LicenseService extends GenericService<License, Integer> {

	/**
	 * Saves the license into the database and pairs the newly created license with the specified 
	 * experiment package
	 * @param license
	 * @param group 
	 */
	public void addLicenseForPackage(License license, ExperimentPackage group);
	
	/**
	 * Detaches License object from given ExperimentPackage. Deletes from database only the connection, both objects remain stored.
	 * @param license
	 * @param group 
	 */
	public void removeLicenseFromPackage(License license, ExperimentPackage group);

	/**
	 * Returns all licenses of a given type.
	 * @param type Fetch only licenses of this type.
	 * @return list of licenses that match the given criteria.
	 */
	public List<License> getLicensesByType(LicenseType type);

	/**
	 * Returns all licenses of given types.
	 * @param type Fetch only licenses of specified types
	 * @return list of licenses that match the given criteria.
	 */
	public List<License> getLicensesByType(List<LicenseType> type);
	
	/**
	 * List all licenses the package has been published under.
	 *
	 * @param pckg the package
	 * @return list of licenses
	 */
	public List<License> getLicensesForPackage(ExperimentPackage pckg);
	
	public byte[] getLicenseAttachmentContent(int licenseId);
	
	public List<License> getLicenseForPackageAndOwnedByPerson(int personId, int packageId);
	
	public License getLicenseForPurchasedExperiment(int experimentId, int personId);
    
    public License getLicenseForPurchasedExpPackage(int experimentPackageId, int personId);

    public List<License> getLicensesForExperiment(int experimentId);

    public List<License> getPersonLicenses(int personId);
    
}
