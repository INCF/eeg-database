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
 *   PeopleSearcherCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 * @author pbruha
 */
public class PeopleSearcherCommand {

    private int givennameOption;
    private String givenname;
    private int surnameOption;
    private String surname;
    private int emailOption;
    private String email;
    private int genderOption;
    private String gender;
    private int ageOption;
    private String fromDate;
    private String toDate;
    private int withoutDefectOption;

    public int getAgeOption() {
        return ageOption;
    }

    public void setAgeOption(int ageOption) {
        this.ageOption = ageOption;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailOption() {
        return emailOption;
    }

    public void setEmailOption(int emailOption) {
        this.emailOption = emailOption;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGenderOption() {
        return genderOption;
    }

    public void setGenderOption(int genderOption) {
        this.genderOption = genderOption;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public int getGivennameOption() {
        return givennameOption;
    }

    public void setGivennameOption(int givennameOption) {
        this.givennameOption = givennameOption;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSurnameOption() {
        return surnameOption;
    }

    public void setSurnameOption(int surnameOption) {
        this.surnameOption = surnameOption;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getWithoutDefectOption() {
        return withoutDefectOption;
    }

    public void setWithoutDefectOption(int withoutDefectOption) {
        this.withoutDefectOption = withoutDefectOption;
    }
}
