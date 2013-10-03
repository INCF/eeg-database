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
 *   BookRoomCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * @author Jenda Kolena
 */
public class BookRoomCommand {

    private int selectedGroup;
    private String date;
    private String startTime;
    private GregorianCalendar startTimeCal;
    private String endTime;
    private GregorianCalendar endTimeCal;
    private int repCount;
    private int repType;

    public void setSelectedGroup(int selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public int getSelectedGroup() {
        return selectedGroup;
    }


    public void setDate(String date) throws Exception {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRepCount(int repCount) {
        this.repCount = repCount;
    }

    public int getRepCount() {
        return repCount;
    }

    public void setRepType(int repType) {
        this.repType = repType;
    }

    public int getRepType() {
        return repType;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        endTimeCal = BookingRoomUtils.getCalendar(endTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public GregorianCalendar getEndTimeCal() {
        return endTimeCal;
    }


    public Timestamp getEndTimeTimestamp() {
        return new Timestamp(endTimeCal.getTimeInMillis());
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
        startTimeCal = BookingRoomUtils.getCalendar(startTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public GregorianCalendar getStartTimeCal() {
        return startTimeCal;
    }

    public Timestamp getStartTimeTimestamp() {
        return new Timestamp(getStartTimeCal().getTimeInMillis());
    }

}
