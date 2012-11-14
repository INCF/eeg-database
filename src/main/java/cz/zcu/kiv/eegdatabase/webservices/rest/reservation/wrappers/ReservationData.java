package cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers;

import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.utils.DateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * Data container for reservation information.
 *
 * @author Petr Miko
 */
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

    public ReservationData() {
    }

    public ReservationData(int reservationId, String researchGroup, int researchGroupId, Date fromTime, Date toTime, String creatorName, String creatorMail) {
        this.reservationId = reservationId;
        this.researchGroup = researchGroup;
        this.researchGroupId = researchGroupId;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.creatorName = creatorName;

        parseMail(creatorMail);
    }

    private void parseMail(String creatorMail) {
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorMailUsername() {
        return creatorMailUsername;
    }

    public void setCreatorMailUsername(String creatorMailUsername) {
        this.creatorMailUsername = creatorMailUsername;
    }

    public String getCreatorMailDomain() {
        return creatorMailDomain;
    }

    public void setCreatorMailDomain(String creatorMailDomain) {
        this.creatorMailDomain = creatorMailDomain;
    }
}
