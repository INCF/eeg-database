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
 *   SyncChangesInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

import java.sql.Timestamp;

/**
 * Wrapper class for holding last changes important for server-client synchronization.
 *
 * @author: Petr Miko - miko.petr (at) gmail.com
 */
public class SyncChangesInfo {

    private String tableName;
    private long lastChangeInMillis;

    /**
     * Getter of monitored table name.
     *
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Setter of monitored table name.
     *
     * @param tableName table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Getter of time of last change.
     *
     * @return milliseconds of last change
     */
    public long getLastChangeInMillis() {
        return lastChangeInMillis;
    }

    /**
     * Setter of time of last change.
     *
     * @param lastChangeInMillis milliseconds of last change
     */
    public void setLastChangeInMillis(long lastChangeInMillis) {
        this.lastChangeInMillis = lastChangeInMillis;
    }
}
