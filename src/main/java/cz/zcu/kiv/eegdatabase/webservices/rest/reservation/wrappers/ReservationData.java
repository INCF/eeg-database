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
 *   ReservationData.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.utils.DateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * Data container for reservation information.
 *
 * @author Petr Miko
 */
@XmlType
@XmlRootElement(name = "reservation")
public class ReservationData implements Serializable {

    private int reservationId;
    private String researchGroup;
    private int researchGroupId;
    private Date fromTime;
    private Date toTime;
    private String creatorName;
    private String creatorMailUsername;
    private String creatorMailDomain;
    private boolean canRemove;

    public ReservationData() {
    }

    public ReservationData(int reservationId, String researchGroup, int researchGroupId, Date fromTime, Date toTime, String creatorName, String creatorMail, boolean canRemove) {
        this.reservationId = reservationId;
        this.researchGroup = researchGroup;
        this.researchGroupId = researchGroupId;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.creatorName = creatorName;
        this.canRemove = canRemove;

        parseMail(creatorMail);
    }

    public void parseMail(String creatorMail) {
        char mailSeparator = '@';

        if (creatorMail != null && creatorMail.contains(""+mailSeparator)) {
            String[] mail = creatorMail.split(""+mailSeparator);
            if (mail.length == 2) {
                creatorMailUsername = mail[0];
                creatorMailDomain = mail[1];
            }
        }
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    @XmlElement(required = false)
    public String getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(String researchGroup) {
        this.researchGroup = researchGroup;
    }

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    @XmlElement(required = false)
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @XmlElement(required = false)
    public String getCreatorMailUsername() {
        return creatorMailUsername;
    }

    public void setCreatorMailUsername(String creatorMailUsername) {
        this.creatorMailUsername = creatorMailUsername;
    }

    @XmlElement(required = false)
    public String getCreatorMailDomain() {
        return creatorMailDomain;
    }

    public void setCreatorMailDomain(String creatorMailDomain) {
        this.creatorMailDomain = creatorMailDomain;
    }

    @XmlElement(required = false)
    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }
}
