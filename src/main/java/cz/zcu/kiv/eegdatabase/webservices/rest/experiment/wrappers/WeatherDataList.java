package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data container for list of weathers.
 * Required for XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "weatherList")
public class WeatherDataList {

    @XmlElement(name = "weather")
    public List<WeatherData> weatherList;

    public WeatherDataList() {
        this(new ArrayList<WeatherData>());
    }

    public WeatherDataList(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
    }
}
