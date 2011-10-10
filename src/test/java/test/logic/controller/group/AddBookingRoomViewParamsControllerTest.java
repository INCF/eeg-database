package test.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.controller.group.AddBookingRoomViewParamsController;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import test.logic.AbstractControllerTest;
import test.mock.MockFactory;
import test.mock.SimplePersonDao;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * AddBookingRoomViewParamsController Tester.
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 1.0
 * @since <pre>05/04/2011</pre>
 */

public class AddBookingRoomViewParamsControllerTest extends AbstractControllerTest
{

    private AddBookingRoomViewParamsController controller = null;
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

        controller = new AddBookingRoomViewParamsController();
        request = new MockHttpServletRequest();
        request.setRequestURI("/book-room-view.html");
        String[] attrs = ("filterGroup=0&filterDate=&date=" + date + "&startTime=" + date + " " + start + "&endTime=" + date + " " + end + "&repType=0&repCount=0").split("&");
        for (String attr : attrs)
        {
            String[] param = attr.split("=");
            request.addParameter(param[0], param.length > 1 ? param[1] : "");
        }

        controller.setPersonDao(personDao);
        controller.setResearchGroupDao(researchGroupDao);
        controller.setReservationDao(reservationDao);
        controller.setMessageSource(messageSource);

        GregorianCalendar startDate = BookingRoomUtils.getCalendar(date + " " + start);
        GregorianCalendar endDate = BookingRoomUtils.getCalendar(date + " " + end);
        endDate.add(Calendar.HOUR, 2);
        MockFactory.createReservation(startDate, endDate);

        Map data = controller.referenceData(request, null, null);
        assertTrue(data.size() > 0);

        assertEquals(start.replaceAll(":", "").substring(0, 4), data.get("startTime"));
        assertEquals(end.replaceAll(":", "").substring(0, 4), data.get("endTime"));
        assertEquals(date + " " + start.split(":")[0] + ":" + start.split(":")[1] + " - " + end.split(":")[0] + ":" + end.split(":")[1], data.get("timerange"));

        List<Reservation> reservations = reservationDao.getReservationsBetween(startDate, endDate, "", 0);
        assertEquals(((List) data.get("reservations")).size(), reservations.size());

        try
        {
            controller.getResearchGroup(0);
            assertTrue(false);//previous command must throw an exception, so this assertion is not processed
        } catch (Exception e)
        {

        }

        MockFactory.freeAll();
    }

}
