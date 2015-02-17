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
 *   PersonalLicenseService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicenseState;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.util.List;

/**
 *
 * @author J. Danek
 */
public interface PersonalLicenseService extends GenericService<PersonalLicense, Integer> {

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
	 * @return list of PersonalLicenses with the state given
	 */
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group, PersonalLicenseState state);

	/**
	 * Returns list of license requests requested by specified person or empty
	 * list.
	 *
	 * @param applicant Person whose requests will be returned.
	 * @param state  state the requests are at
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
	public boolean addLicenseToPerson(Person person, License license);

	/**
	 * Returns list of all user's licenses (both accepted and awaiting confirmation).
	 * @param person
	 * @return
	 */
	public List<License> getUsersLicenses(Person person);
	
	public byte[] getPersonalLicenseAttachmentContent(int personalLicenseId);
}
