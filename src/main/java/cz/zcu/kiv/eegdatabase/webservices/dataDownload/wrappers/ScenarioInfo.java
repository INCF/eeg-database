package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Class for wrapping the scenario information.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class ScenarioInfo {

    private int scenarioId;
    private int ownerId;
    private int researchGroupId;
    private String title;
    private int scenarioLength;
    private String description;
    private String scenarioName;
    private String mimeType;
    private long scn;

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
     * Getter of owner's identifier.
     *
     * @return owner's identifier
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Setter of owner's identifier.
     *
     * @param ownerId owner's identifier
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Getter of research group id.
     *
     * @return research group id
     */
    public int getResearchGroupId() {
        return researchGroupId;
    }

    /**
     * Setter of research group.
     *
     * @param researchGroupId research group id
     */
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
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

    /**
     * Getter of revision number (oracle scn).
     * @return revision number
     */
    public long getScn() {
        return scn;
    }

    /**
     * Setter of revision number (oracle scn).
     *
     * @param scn revision number
     */
    public void setScn(long scn) {
        this.scn = scn;
    }
}
