package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.Date;
import java.util.List;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;

/**
 * Data Access Object for accessing Reservation entities. The interface will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public interface ReservationDao extends GenericDao<Reservation, Integer> {

  public List<Reservation> getReservationsBetween(Date start, Date end);

}
