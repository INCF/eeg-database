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
 *   RegistrationObject.java, 2015/10/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.security.components;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

/**
 * Created by stebjan on 7.10.2015.
 */
public class RegistrationObject {

    private String password;
    private String passwordVerify;
    private String username;
    private FullPersonDTO panelPerson;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FullPersonDTO getPanelPerson() {
        return panelPerson;
    }

    public void setPanelPerson(FullPersonDTO panelPerson) {
        this.panelPerson = panelPerson;
    }
}
