package cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for Reservation data collection.
 *
 * @author Petr Miko
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
