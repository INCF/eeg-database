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
 *   WeatherInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
