package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import org.hibernate.Session;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Implementation of DAO for accessing Reservation entities. This class will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public class SimpleReservationDao
        extends SimpleGenericDao<Reservation, Integer>
        implements ReservationDao {

    public SimpleReservationDao() {
        super(Reservation.class);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end) {//${(start<myEnd) && (end>myStart)}

        String hqlQuery = "from Reservation reservation where reservation.startTime < (:endtime) AND reservation.endTime >  (:starttime) order by reservation.startTime";

        Session session = getSession();
        return session.createQuery(hqlQuery).setTimestamp("starttime", start.getTime()).setTimestamp("endtime", end.getTime()).list();

    }

}
