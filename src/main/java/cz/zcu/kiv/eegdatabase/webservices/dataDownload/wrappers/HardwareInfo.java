package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

import java.util.List;

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
    private List<Integer> experimentIds;
    private boolean added;
    private boolean changed;

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

    /**
     * Object is meant to create new record.
     * @return new record
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Mark object to create new record.
     * @param added new record
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * Object is meant to update existing record.
     * @return updated record
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Mark object to update an existing object.
     * @param changed updated record
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
       Getter of Experiment ids in which the HW is used.
     */
    public List<Integer> getExperimentIds() {
        return experimentIds;
    }

    /**
       Setter of Experiment ids in which the HW is used.
     * @param experimentIds list of experiment ids
     */
    public void setExperimentIds(List<Integer> experimentIds) {
        this.experimentIds = experimentIds;
    }
}
