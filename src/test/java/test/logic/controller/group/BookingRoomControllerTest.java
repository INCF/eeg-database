package test.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.controller.group.BookRoomCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.group.BookingRoomController;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import test.logic.AbstractControllerTest;
import test.mock.MockFactory;
import test.mock.SimplePersonDao;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * BookingRoomController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>05/04/2011</pre>
 */
public class BookingRoomControllerTest extends AbstractControllerTest
{
    private BookingRoomController controller = null;
    private MockHttpServletRequest request;

    @Test
    public void testOnSubmit() throws Exception
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

        controller = new BookingRoomController();

        controller.setPersonDao(personDao);
        controller.setResearchGroupDao(researchGroupDao);
        controller.setReservationDao(reservationDao);
        controller.setMessageSource(messageSource);

        BookRoomCommand command = new BookRoomCommand();
        command.setDate(date);
        command.setStartTime(date + " " + start);
        command.setEndTime(date + " " + end);
        command.setRepCount(0);
        command.setRepType(0);
        ResearchGroup researchGroup = MockFactory.createResearchGroup();
        command.setSelectedGroup(researchGroup.getResearchGroupId());

        request = new MockHttpServletRequest();
        request.setRequestURI("/book-room.html");

        assertNotNull(controller.onSubmit(request, null, command, null));

        GregorianCalendar startDate = BookingRoomUtils.getCalendar(date + " " + start);
        GregorianCalendar endDate = BookingRoomUtils.getCalendar(date + " " + end);
        List<Reservation> reservations = reservationDao.getReservationsBetween(startDate, endDate, "", 0);
        assertTrue(reservations.size() > 0);
        Reservation reservation = reservations.get(0);

        reservationDao.delete(reservation);

        Map data = controller.referenceData(null);
        assertTrue(!data.get("status").equals(""));
        assertTrue(!data.get("comment").equals(""));

        MockFactory.freeAll();
    }
}
