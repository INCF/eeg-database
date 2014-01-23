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
 *   HardwareInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.datadownload.wrappers;

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
