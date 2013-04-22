/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.util.List;

/**
 *
 * @author bydga
 */
public interface PersonalLicenseDao extends GenericDao<PersonalLicense, Integer> {
	
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group);
	
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted);  
}
