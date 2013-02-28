package cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 9.2.13
 */
@XmlRootElement(name = "reservations")
public class ReservationDataList {

    @XmlElement(name = "reservation")
    public List<ReservationData> reservations;

    public ReservationDataList() {
        this(Collections.<ReservationData>emptyList());
    }

    public ReservationDataList(List<ReservationData> reservations) {
        this.reservations = reservations;
    }
}
