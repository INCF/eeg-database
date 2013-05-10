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
	 * @return list of PersonalLicenses NOT confirmed by admin or unpaid, or
	 * empty list
	 */
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group);

	/**
	 *
	 * @param group research group to search by
	 * @return list of PersonalLicenses confirmed by admin or paid, or empty
	 * list
	 */
	public List<PersonalLicense> getGrantedLicenses(ResearchGroup group);

	/**
	 * Returns list of license requests requested by specified person or empty
	 * list.
	 *
	 * @param applicant Person whose requests will be returned.
	 * @param accepted Flag determining, what kind of requests should be
	 * fetched. True means only accepted (active) PersonalLicenses will be
	 * returned. False means only pending licenses will be returned.
	 * @return
	 */
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted);

	/**
	 * Adds a single license to the person.
	 *
	 * @param license license to be added
	 * @param person person
	 * @return true if success, false if not (e.g. person already has the
	 * license)
	 */
	public boolean addLicenseToPerson(Person person, License license);
}
