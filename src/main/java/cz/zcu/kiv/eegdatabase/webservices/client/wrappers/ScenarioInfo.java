package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * @author František Liška
 */
public class ScenarioInfo {

    private int scenarioId;
    private String ownerUsername;
    private String title;
    private int scenarioLength;
    private boolean privateScenario;
    private String researchGroupTitle;
    private String description;
    private String scenarioName;
    private String mimeType;

    /**
     * Getter of scenario identifier.
     *
     * @return scenario identifier
     */
    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * Setter of scenario identifier.
     *
     * @param scenarioId scenario identifier
     */
    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * Getter of scenario's title.
     *
     * @return scenario's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter of scenario's title.
     *
     * @param title scenario's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of scenario's length.
     *
     * @return scenario's length
     */
    public int getScenarioLength() {
        return scenarioLength;
    }

    /**
     * Setter of scenario's length.
     *
     * @param scenarioLength scenario's length
     */
    public void setScenarioLength(int scenarioLength) {
        this.scenarioLength = scenarioLength;
    }

    /**
     * Getter of scenario's closer description.
     *
     * @return scenario's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of closer scenario's description.
     *
     * @param description scenario's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of scenario's name.
     *
     * @return scenario name
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Setter of scenario's name.
     *
     * @param scenarioName scenario name
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * Getter of MIME type.
     *
     * @return String of MIME type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Setter of MIME type.
     *
     * @param mimeType String of MIME type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public boolean isPrivateScenario() {
        return privateScenario;
    }

    public void setPrivateScenario(boolean privateScenario) {
        this.privateScenario = privateScenario;
    }

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
