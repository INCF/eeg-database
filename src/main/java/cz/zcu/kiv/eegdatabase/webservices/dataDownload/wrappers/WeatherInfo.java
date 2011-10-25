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
}
