package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * Class for gathering important information about scenario. Meant to be sent
 * between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class ScenarioInfo {
	private int scenarioId;
	private int ownerPersonId;
	private String title;
	private int scenarioLength;
	private long fileLength;
	private boolean privateScenario;
	private int researchGroupId;
	private String description;
	private String scenarioName;
	private String mimetype;

	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScenarioLength() {
		return scenarioLength;
	}

	public void setScenarioLength(int scenarioLength) {
		this.scenarioLength = scenarioLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getMimetype() {
		return mimetype;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public boolean isPrivateScenario() {
		return privateScenario;
	}

	public void setPrivateScenario(boolean privateScenario) {
		this.privateScenario = privateScenario;
	}

	public int getResearchGroupId() {
		return researchGroupId;
	}

	public void setResearchGroupId(int researchGroupId) {
		this.researchGroupId = researchGroupId;
	}

	public int getOwnerPersonId() {
		return ownerPersonId;
	}

	public void setOwnerPersonId(int ownerPersonId) {
		this.ownerPersonId = ownerPersonId;
	}
}
