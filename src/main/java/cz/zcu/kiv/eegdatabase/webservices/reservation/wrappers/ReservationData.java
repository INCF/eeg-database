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

	private String username;
	private Date fromTime;
	private Date toTime;

    public ReservationData(){}

	public ReservationData(String username, Date fromTime, Date toTime) {
		this.username = username;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
