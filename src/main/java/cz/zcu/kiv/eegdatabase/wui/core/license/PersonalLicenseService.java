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
}
