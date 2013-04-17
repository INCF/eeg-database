/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.io.Serializable;

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
}
