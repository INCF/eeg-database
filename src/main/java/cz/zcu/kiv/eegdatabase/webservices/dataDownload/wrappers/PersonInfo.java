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
 *   PersonInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Data wrapper for shared person information.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class PersonInfo {

    private int personId;
    private int defaultGroupId;
    private String givenName;
    private String surname;
    private char gender;
    private boolean changed;
    private boolean added;

    /**
     * Getter of person's identifier.
     *
     * @return person's identifier
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Setter of person's identifier.
     *
     * @param personId person's identifier
     */
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    /**
     * Getter of default group's identifier.
     *
     * @return default group identifier
     */
    public int getDefaultGroupId() {
        return defaultGroupId;
    }

    /**
     * Setter of default group's identifier.
     *
     * @param defaultGroupId default group identifier
     */
    public void setDefaultGroupId(int defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    /**
     * Getter of person's given name.
     *
     * @return given name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Setter of person's given name.
     *
     * @param givenName given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Getter of person's surname
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter of person's surname.
     *
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Getter of person's gender.
     *
     * @return M/F as male/female
     */
    public char getGender() {
        return gender;
    }

    /**
     * Setter of person's gender
     *
     * @param gender M/F as male/female
     */
    public void setGender(char gender) {
        this.gender = gender;
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
