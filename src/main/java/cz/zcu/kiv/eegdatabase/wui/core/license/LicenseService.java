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
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
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
	 * Returns the one global public License object.
	 * @return global shared public license.
	 */
	public License getPublicLicense();
	public byte[] getPublicLicenseFile();
	public String getPublicLicenseFileName();

	/**
	 * Returns all licenses of a given type for specified group.
	 * @param group Group to get licenses for
	 * @param type Fetch only licenses of this type.
	 * @return list of licenses that match the given criteria.
	 */
	public List<License> getLicensesForGroup(ResearchGroup group, LicenseType type);

	/**
	 * Returns all licenses of a given types for specified group.
	 * @param group Group to get licenses for
	 * @param type Fetch only licenses of specified types
	 * @return list of licenses that match the given criteria.
	 */
	public List<License> getLicensesForGroup(ResearchGroup group, List<LicenseType> type);

	/**
	 * Returns group specific owner license. This licese is used to share experiments inside ResearchGroup.
	 * @param group Whose license to fetch.
	 * @return Group's owner license.
	 */
	public License getOwnerLicense(ResearchGroup group);
	
	/**
	 * List all licenses the package has been published under.
	 *
	 * @param pckg the package
	 * @return list of licenses
	 */
	public List<License> getLicensesForPackage(ExperimentPackage pckg);

	/**
	 *
	 * @return list of licenses saved as templates for new
	 */
	public List<License> getLicenseTemplates(ResearchGroup group);
	
	public byte[] getLicenseAttachmentContent(int licenseId);
	
	public List<License> getLicenseForPackageAndOwnedByPerson(int personId, int packageId);
	
	public License getLicenseForPurchasedExperiment(int experimentId, int personId);
    
    public License getLicenseForPurchasedExpPackage(int experimentPackageId, int personId);
}
