package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Class for gathering few important information about hardware.
 * Meant to be sent to user.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class HardwareInfo {

    private String description;
    private int hardwareId;
    private long scn;
    private String title;
    private String type;

    /**
     * Getter of HW description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of HW description.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of HW identifier.
     *
     * @return HW identifier
     */
    public int getHardwareId() {
        return hardwareId;
    }

    /**
     * Setter of HW identifier.
     *
     * @param hardwareId
     */
    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
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

    /**
     * Getter of HW title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter of HW title.
     *
     * @param title HW title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of HW type.
     *
     * @return HW type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter of HW type.
     *
     * @param type HW type
     */
    public void setType(String type) {
        this.type = type;
    }
}
