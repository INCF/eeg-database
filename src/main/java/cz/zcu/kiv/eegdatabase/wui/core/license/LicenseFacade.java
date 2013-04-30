package cz.zcu.kiv.eegdatabase.wui.core.license;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author j. Danek
 */
public interface LicenseFacade extends GenericFacade<License, Integer> {
	
	public void addLicenseForPackage(License license, ExperimentPackage group);
	
	public void removeLicenseFromPackage(License license, ExperimentPackage group);
	
	public void createRequestForLicense(PersonalLicense personalLicense);
	
	public void confirmRequestForLicense(PersonalLicense personalLicense);
	
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group);
	
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted);

	public boolean addLicenseToPerson(License license, Person person);

	public boolean addPublicLicenseToPerson(Person person);

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
