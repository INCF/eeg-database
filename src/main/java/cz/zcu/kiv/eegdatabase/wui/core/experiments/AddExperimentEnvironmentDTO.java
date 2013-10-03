/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AddExperimentEnvironmentDTO.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentEnvironmentDTO extends IdentifiDTO implements Serializable {
    private String weatherNote;
    private Double temperature;
    private String disease;
    private String pharmaceutical;
    private String privateNote;
    private Collection<String> hardware;
    private String weather;
    private Collection<String> software;

    public Collection<String> getSoftware() {
        return software;
    }

    public void setSoftware(Collection<String> software) {
        this.software = software;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Collection<String> getHardware() {
        return hardware;
    }

    public void setHardware(Collection<String> hardware) {
        this.hardware = hardware;
    }


    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
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
