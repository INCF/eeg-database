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
 *   LicenseFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import java.util.List;

/**
 *
 * @author J. Danek
 */
public interface LicenseFacade extends GenericFacade<License, Integer> {

	/**
	 * Save changes of existing PersonalLicense instance
	 * @param license
	 */
	public void updatePersonalLicense(PersonalLicense license);
	
	/**
	 * Detaches License object from given ExperimentPackage. Deletes from database only the connection, both objects remain stored.
	 * @param license
	 * @param pckg 
	 */
	public void removeLicenseFromPackage(License license, ExperimentPackage pckg);
	
	/**
	 * Saves into database new PersonalLicense (only yet inactive request).
	 *
	 * @param personalLicense The object to be saved.
	 */
	public void createRequestForLicense(PersonalLicense personalLicense);
	
	/**
	 * Accepts Lincese request (specified by PersonalLicense object). Sets acceptedDate flag and the license will become active.
	 * Also sends info email to the applicant.
	 *
	 * @param personalLicense License to be rejected.
	 */
	public void confirmRequestForLicense(PersonalLicense personalLicense);

	/**
	 * Rejects Lincese request (specified by PersonalLicense object). Deletes it
	 * from database and sends info email to the applicant.
	 *
	 * @param personalLicense License to be rejected.
	 */
	public void rejectRequestForLicense(PersonalLicense personalLicense);

	/**
	 *
	 * @param group research group to search by
	 * @return list of PersonalLicenses NOT confirmed by admin or unpaid, or empty list
	 */
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group);

	/**
	 *
	 * @param group research group to search by
	 * @return list of PersonalLicenses confirmed by admin or paid, or empty list
	 */
	public List<PersonalLicense> getGrantedLicenses(ResearchGroup group);

	/**
	 *
	 * @param group
	 * @return list of PersonalLicense requests revoked by admin
	 */
	public List<PersonalLicense> getRevokedRequests(ResearchGroup group);
	
	/**
	 * Returns list of license requests requested by specified person or empty
	 * list.
	 *
	 * @param applicant Person whose requests will be returned.
	 * @param state state the licenses are supposed to have
	 * @return
	 */
	public List<PersonalLicense> getLicenseRequests(Person applicant, PersonalLicenseState state);

	/**
	 * Adds a single license to the person.
	 *
	 * @param license license to be added
	 * @param person person
	 * @return true if success, false if not (e.g. person already has the
	 * license)
	 */
	public boolean addLicenseToPerson(License license, Person person);

	/**
	 * Returns all licenses of a given type.
	 * @param type Fetch only licenses of this type.
	 * @return list of licenses that match the given criteria.
	 */
	public List<License> getLicensesByType(LicenseType type);

	/**
	 * Returns all licenses of given types.
	 * @param type Fetch only licenses of specified types.
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

	/**
	 * Returns list of all user's licenses (both accepted and awaiting confirmation).
	 * @param person
	 * @return
	 */
	public List<License> getUsersLicenses(Person person);
	
	public byte[] getPersonalLicenseAttachmentContent(int personalLicenseId);
	
	public byte[] getLicenseAttachmentContent(int licenseId);
	
	public List<License> getLicenseForPackageAndOwnedByPerson(int personId, int packageId);
	
	public License getLicenseForPurchasedExperiment(int experimentId, int personId);
	
	public License getLicenseForPurchasedExpPackage(int experimentPackageId, int personId);

    public List<License> getLicensesForExperiment(int experimentId);

    public List<License> getPersonLicenses(int personId);

    void removeLicenseFromExperiment(Experiment exp, License license);
}
