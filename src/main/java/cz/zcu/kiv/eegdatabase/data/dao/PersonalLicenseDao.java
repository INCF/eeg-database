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
 *   PersonalLicenseDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicenseState;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.List;

/**
 * Data-access object interface for PersonalLicense entity.
 *
 * @author bydga
 */
public interface PersonalLicenseDao extends GenericDao<PersonalLicense, Integer> {

	/**
	 * Finds license requests for research group.
	 * @param group
	 * @param state state the requests are at
	 * @return
	 */
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group, PersonalLicenseState state);

	/**
	 * Finds license requests for person.
	 * @param group
	 * @param state state the requests are at
	 * @return
	 */
	public List<PersonalLicense> getLicenseRequests(Person applicant, PersonalLicenseState state);
	
	public byte[] getAttachmentContent(int personalLicenseId);

    /**
     * Checks whether the person has already a license.
     * @param experimentId id of the experiment
     * @param packageId id of the package
     * @return true if person has the license
     */
    public boolean personHasLicense(int personId, int licenseId);

	/**
	 * Returns list of all user's licenses (both accepted and awaiting confirmation).
	 * @param person
	 * @return
	 */
	public List<License> getUsersLicenses(Person person);
}
