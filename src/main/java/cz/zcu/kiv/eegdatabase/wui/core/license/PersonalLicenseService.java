/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

/**
 *
 * @author veveri
 */
public interface PersonalLicenseService extends GenericService<PersonalLicense, Integer> {
	
	public void addLicenseToPerson(Person p, License license);
			

}
