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
 * @author veveri
 */
public interface LicenseService extends GenericService<License, Integer> {

	/**
	 * Saves the license into the database and pairs the newly created license with the specified 
	 * experiment package
	 * @param license
	 * @param group 
	 */
	public void addLicenseForPackage(License license, ExperimentPackage group);
	
	public void removeLicenseFromPackage(License license, ExperimentPackage group);
		
	public License getPublicLicense();

	public List<License> getLicensesForGroup(ResearchGroup group, LicenseType type);

	public List<License> getLicensesForGroup(ResearchGroup group, List<LicenseType> type);

	public License getOwnerLicense(ResearchGroup group);
	
	/**
	 * List all licenses the package has been published under.
	 *
	 * @param pckg the package
	 * @return list of licenses
	 */
	public List<License> getLicensesForPackage(ExperimentPackage pckg);
}
