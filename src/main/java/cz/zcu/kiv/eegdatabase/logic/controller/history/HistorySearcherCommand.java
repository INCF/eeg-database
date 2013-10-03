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
 *   HistorySearcherCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.history;

import java.sql.Timestamp;

/**
 * @author pbruha
 */
public class HistorySearcherCommand {
    private String title;
    private Timestamp fromDateOfDownload;
    private Timestamp toDateOfDownload;

    public Timestamp getFromDateOfDownload() {
        return fromDateOfDownload;
    }

    public void setFromDateOfDownload(Timestamp fromDateOfDownload) {
        this.fromDateOfDownload = fromDateOfDownload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getToDateOfDownload() {
        return toDateOfDownload;
    }

    public void setToDateOfDownload(Timestamp toDateOfDownload) {
        this.toDateOfDownload = toDateOfDownload;
    }


}
