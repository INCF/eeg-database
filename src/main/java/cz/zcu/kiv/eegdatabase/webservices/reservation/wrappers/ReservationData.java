package cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers;

import cz.zcu.kiv.eegdatabase.webservices.reservation.utils.DateAdapter;

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

    public ReservationData() {
    }

    public ReservationData(int reservationId, String researchGroup, int researchGroupId, Date fromTime, Date toTime) {
        this.reservationId = reservationId;
        this.researchGroup = researchGroup;
        this.researchGroupId = researchGroupId;
        this.fromTime = fromTime;
        this.toTime = toTime;
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
}
