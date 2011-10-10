package test.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import test.logic.AbstractControllerTest;
import test.mock.MockFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * SimpleReservationDao Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>05/04/2011</pre>
 */
public class SimpleReservationDaoTest extends AbstractControllerTest
{
    /**
     * Method: getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String date, int group)
     */
    public void testGetReservationsBetween() throws Exception
    {
        assertNotNull(reservationDao);

        GregorianCalendar start = new GregorianCalendar();
        start.set(Calendar.YEAR, 2100);
        GregorianCalendar end = new GregorianCalendar();
        end.set(Calendar.YEAR, 2100);
        end.add(Calendar.HOUR, 2);
        MockFactory.setDaoCollection(getDaoCollection());
        MockFactory.createReservation(start, end);

        List<Reservation> reservations = reservationDao.getReservationsBetween(start, end);
        assertTrue(reservations.size() == 1);

        MockFactory.freeAll();
    }

    /**
     * Method: getReservationById(int id)
     *
     * @throws Exception
     */
    @Test
    public void testGetReservationById() throws Exception
    {
        assertNotNull(reservationDao);
        assertNull(reservationDao.getReservationById(0));

        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        end.add(Calendar.HOUR, 2);
        MockFactory.setDaoCollection(getDaoCollection());
        Reservation reservation = MockFactory.createReservation(start, end);

        List<Reservation> reservations = reservationDao.getReservationsBetween(start, end);
        assertTrue("Error in getReservationsBetween while testing getReservationById!", reservations.size() >= 1);

        int id = reservations.get(0).getReservationId();
        reservation = reservationDao.getReservationById(id);

        assertEquals(reservations.get(0).getCreationTime(), reservation.getCreationTime());

        MockFactory.freeAll();
    }
}
