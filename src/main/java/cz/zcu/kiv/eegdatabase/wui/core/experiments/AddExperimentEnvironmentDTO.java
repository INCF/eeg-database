package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentEnvironmentDTO extends IdentifiDTO implements Serializable {
    private String weatherNote;
    private Integer temperature;
    private String disease;
    private String pharmaceutical;
    private String privateNote;
    private String hardware;
    private String weather;
    private String software;

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }


    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPharmaceutical() {
        return pharmaceutical;
    }

    public void setPharmaceutical(String pharmaceutical) {
        this.pharmaceutical = pharmaceutical;
    }

    public String getPrivateNote() {
        return privateNote;
    }

    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
    }
}
