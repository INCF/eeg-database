package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Data Access Object for accessing Reservation entities. The interface will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public interface ReservationDao extends GenericDao<Reservation, Integer>
{
    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String date, int group);

    public Reservation getReservationById(int id);

    public boolean deleteReservation(int id);
}