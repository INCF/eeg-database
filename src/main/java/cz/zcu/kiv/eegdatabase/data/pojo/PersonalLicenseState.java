package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * States a PersonalLicense can be at.
 *
 * @author Jakub Danek
 */
public enum PersonalLicenseState {
	/**
	 * Application has been rejected.
	 */
	REJECTED,
	/**
	 * The person has submitted an application for the license.
	 */
	APPLICATION,
	/**
	 * The person has been granted the license.
	 */
	AUTHORIZED
}
