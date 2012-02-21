package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Class for wrapping weather information.
 * <p/>
 * User: Petr
 * Date: 17.10.11
 */
public class WeatherInfo {

    private int weatherId;
    private String description;
    private String title;
    private long scn;
    private boolean changed;
    private boolean added;

    /**
     * Getter of weather identifier.
     *
     * @return weather identifier
     */
    public int getWeatherId() {
        return weatherId;
    }

    /**
     * Setter of weather id.
     *
     * @param weatherId weather id
     */
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /**
     * Getter of weather description.
     *
     * @return weather description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of weather description.
     *
     * @param description weather description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of weather title.
     *
     * @return weather title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter of weather title.
     *
     * @param title weather title
     */
    public void setTitle(String title) {
        this.title = title;
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
}
