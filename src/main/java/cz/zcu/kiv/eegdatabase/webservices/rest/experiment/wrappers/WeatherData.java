package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "weather", propOrder = {"weatherId", "title", "description"})
public class WeatherData {

    private int weatherId;
    private String title;
    private String description;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
