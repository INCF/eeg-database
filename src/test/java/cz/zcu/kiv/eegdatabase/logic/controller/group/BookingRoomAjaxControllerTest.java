package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import cz.zcu.kiv.eegdatabase.logic.AbstractControllerTest;
import cz.zcu.kiv.eegdatabase.mock.MockFactory;
import cz.zcu.kiv.eegdatabase.mock.SimplePersonDao;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * BookingRoomAjaxController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>05/04/2011</pre>
 */
public class BookingRoomAjaxControllerTest extends AbstractControllerTest
{
    private BookingRoomAjaxController controller = null;
    private MockHttpServletRequest request;

    @Test
    public void testReferenceData() throws Exception
    {
        String date = "24/03/55";
        String start = "06:00:00";
        String end = "07:00:00";

        MockFactory.setDaoCollection(getDaoCollection());

        SimplePersonDao personDao = new SimplePersonDao(this.personDao);

        if (personDao == null) throw new ExceptionInInitializerError();
        if (researchGroupDao == null) throw new ExceptionInInitializerError();
        if (reservationDao == null) throw new ExceptionInInitializerError();
        if (messageSource == null) throw new ExceptionInInitializerError();

        GregorianCalendar startDate = BookingRoomUtils.getCalendar(date + " " + start);
        GregorianCalendar endDate = BookingRoomUtils.getCalendar(date + " " + end);
        //endDate.add(Calendar.HOUR, 2);//Would cause the test to fail
        MockFactory.createReservation(startDate, endDate);

        List<Reservation> reservations = reservationDao.getReservationsBetween(startDate, endDate, "", 0);
        assertTrue(reservations.size() > 0);
        Reservation reservation = reservations.get(0);

        controller = new BookingRoomAjaxController();
        request = new MockHttpServletRequest();
        request.setRequestURI("/book-room-ajax.html");
        String[] attrs = ("type=info&id=" + reservation.getReservationId()).split("&");
        for (String attr : attrs)
        {
            String[] param = attr.split("=");
            request.addParameter(param[0], param.length > 1 ? param[1] : "");
        }

        controller.setPersonDao(personDao);
        controller.setResearchGroupDao(researchGroupDao);
        controller.setReservationDao(reservationDao);
        controller.setMessageSource(messageSource);

        Map data = (Map) controller.referenceData(request, null, null).get("data");

        MockFactory.freeAll();

        assertTrue(data.size() > 0);

        assertEquals(reservation.getReservationId(), data.get("id"));
        assertEquals(BookingRoomUtils.getDate(startDate), data.get("date"));
        assertEquals(BookingRoomUtils.getHoursAndMinutes(startDate), data.get("start"));
        assertEquals(BookingRoomUtils.getHoursAndMinutes(endDate), data.get("end"));
    }

}
