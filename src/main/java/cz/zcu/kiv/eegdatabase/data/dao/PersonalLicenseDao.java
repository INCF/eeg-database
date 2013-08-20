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
