package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
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
        implements ReservationDao
{

    public SimpleReservationDao()
    {
        super(Reservation.class);
    }

    @SuppressWarnings("unchecked")
    public int createChecked(Reservation newInstance) throws DaoException {

        if(newInstance.getStartTime().getTime() >= newInstance.getEndTime().getTime()){
            throw new DaoException("Time of start cannot be lower than or equal to time of end!");
        }

        String query = "from Reservation reservation where (reservation.startTime <= :startTime and reservation.endTime >= :startTime)" +
                "or (reservation.startTime <= :endTime and reservation.endTime >= :endTime)" +
                "or (reservation.startTime >= :startTime and reservation.endTime <= :endTime)";

        List<Reservation> reservationsInRange = getSession().createQuery(query)
                .setTimestamp("startTime", newInstance.getStartTime())
                .setTimestamp("endTime", newInstance.getEndTime())
                .list();

        if(reservationsInRange.size() > 0){
            throw new DaoException("Reservation could not be created due to existing records within the time range.");
        }

        return super.create(newInstance);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end)
    {
        return getReservationsBetween(start, end, "", 0);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String date, int group)
    {
        if (date != null && date.compareTo("") != 0)
        {//filter of date
            start = BookingRoomUtils.getCalendar(date + " 00:00:00");
            end = BookingRoomUtils.getCalendar(date + " 23:59:59");
        }

        String filterGroup = "";
        if (group > 0)
        {
            filterGroup = " AND reservation.researchGroup = " + group;
        }

        String hqlQuery = "from Reservation reservation where reservation.startTime < (:endtime) AND reservation.endTime > (:starttime)" + filterGroup + " order by reservation.startTime";
        Session session = getSession();
        return session.createQuery(hqlQuery).setTimestamp("starttime", start.getTime()).setTimestamp("endtime", end.getTime()).list();
    }

    public Reservation getReservationById(int id)
    {
        String hqlQuery = "from Reservation reservation where reservation.reservationId = :id";
        Session session = getSession();
        List res = session.createQuery(hqlQuery).setInteger("id", id).list();
        return res.size() == 1 ? (Reservation) res.get(0) : null;
    }

    public boolean deleteReservation(int id)
    {
        String hqlQuery = "delete from Reservation reservation where reservation.reservationId = :id";
        Session session = getSession();
        return session.createQuery(hqlQuery).setInteger("id", id).executeUpdate() == 1;
    }
}
