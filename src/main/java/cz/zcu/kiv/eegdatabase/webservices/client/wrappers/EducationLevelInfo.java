package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * Class for gathering important information about education level. Meant to be
 * sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class EducationLevelInfo {
	private int educationLevelId;
	private String title;
	private int defaultNumber;

	public int getDefaultNumber() {
		return defaultNumber;
	}

	public void setDefaultNumber(int defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getEducationLevelId() {
		return educationLevelId;
	}

	public void setEducationLevelId(int educationLevelId) {
		this.educationLevelId = educationLevelId;
	}

}
