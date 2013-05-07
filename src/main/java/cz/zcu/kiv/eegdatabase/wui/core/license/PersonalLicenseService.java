/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.util.List;

/**
 *
 * @author veveri
 */
public interface PersonalLicenseService extends GenericService<PersonalLicense, Integer> {
	
	public void createRequestForLicense(PersonalLicense personalLicense);
	
	public void confirmRequestForLicense(PersonalLicense personalLicense);
	
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group);
	
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted); 
			

	/**
     * Adds a single license to the person.
     * @param license license to be added
     * @param person person
     * @return true if success, false if not (e.g. person already has the license)
     */
    public boolean addLicenseToPerson(Person person, License license);

	public void rejectRequestForLicense(PersonalLicense personalLicense);
	
}
