package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacadeImpl;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author J. Danek
 */
public class LicenseFacadeImpl extends GenericFacadeImpl<License, Integer> implements LicenseFacade {

	
	private LicenseService licenseService;

	private PersonalLicenseService personalLicenseService;
	
    public LicenseFacadeImpl() {
    }

    public LicenseFacadeImpl(GenericService<License, Integer> service) {
	super(service);
    }
	
	@Required
	public void setLicenseService(LicenseService licenseRervice)
	{
		this.licenseService = licenseRervice;
	}
	
	@Required
	public void setPersonalLicenseService(PersonalLicenseService personalLicenseService)
	{
		this.personalLicenseService = personalLicenseService;
	}

	@Override
	public void addLicenseForPackage(License license, ExperimentPackage pack) {
		this.licenseService.addLicenseForPackage(license, pack);
	}

	@Override
	public void removeLicenseFromPackage(License license, ExperimentPackage pack) {
		this.licenseService.removeLicenseFromPackage(license, pack);
	}

	@Override
	public void createRequestForLicense(PersonalLicense personalLicense) {
		this.personalLicenseService.createRequestForLicense(personalLicense);
	}

	@Override
	public void confirmRequestForLicense(PersonalLicense personalLicense) {
		this.personalLicenseService.confirmRequestForLicense(personalLicense);
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(ResearchGroup group) {
		return this.personalLicenseService.getLicenseRequests(group);
	}

	@Override
	public List<PersonalLicense> getLicenseRequests(Person applicant, boolean accepted) {
		return this.personalLicenseService.getLicenseRequests(applicant, accepted);
	}

	@Override
	public boolean addLicenseToPerson(License licence, Person person) {
		return this.personalLicenseService.addLicenseToPerson(person, licence);
	}

	@Override
	public boolean addPublicLicenseToPerson(Person person) {
		License license = licenseService.getPublicLicense();
		return this.personalLicenseService.addLicenseToPerson(person, license);
	}

	@Override
	public License getPublicLicense() {
		return licenseService.getPublicLicense();
	}

	@Override
	public List<License> getLicensesForGroup(ResearchGroup group, LicenseType type) {
		return licenseService.getLicensesForGroup(group, type);
	}

	@Override
	public List<License> getLicensesForGroup(ResearchGroup group, List<LicenseType> type) {
		return licenseService.getLicensesForGroup(group, type);
	}

	@Override
	public License getOwnerLicense(ResearchGroup group) {
		return licenseService.getOwnerLicense(group);
	}

	@Override
	public List<License> getLicensesForPackage(ExperimentPackage pckg) {
		return licenseService.getLicensesForPackage(pckg);
	}

	@Override
	public void rejectRequestForLicense(PersonalLicense personalLicense) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
