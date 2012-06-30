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
 *
 */
@XmlRootElement(name="reservation")
public class ReservationData implements Serializable {

	private String researchGroup;
	private Date fromTime;
	private Date toTime;

    public ReservationData(){}

	public ReservationData(String researchGroup, Date fromTime, Date toTime) {
		this.researchGroup = researchGroup;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public String getResearchGroup() {
		return researchGroup;
	}

	public void setResearchGroup(String researchGroup) {
		this.researchGroup = researchGroup;
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
