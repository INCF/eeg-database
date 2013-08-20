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
}
