package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomXmlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.naming.directory.InvalidAttributeValueException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class BookingRoomAjaxController
        extends SimpleFormController
{

    private Log log = LogFactory.getLog(getClass());

    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private HierarchicalMessageSource messageSource;

    public BookingRoomAjaxController()
    {
        setCommandClass(BookRoomCommand.class);
        setCommandName("bookRoomCommand");
    }

    @Override
    public Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception
    {
        Map map = new HashMap<String, Object>();

        if (request.getParameter("type").compareTo("info") == 0)
        {
            int id = Integer.parseInt(request.getParameter("id"));
            Reservation reservation = reservationDao.getReservationById(id);
            GregorianCalendar created = new GregorianCalendar();
            created.setTime(reservation.getCreationTime());
            GregorianCalendar startTime = new GregorianCalendar();
            startTime.setTime(reservation.getStartTime());
            GregorianCalendar endTime = new GregorianCalendar();
            endTime.setTime(reservation.getEndTime());

            Map data = new HashMap<String, Object>();
            data.put("id", id);
            data.put("person", reservation.getPerson());
            data.put("created", BookingRoomUtils.getDate(created) + ", " + BookingRoomUtils.getTime(created));
            data.put("date", BookingRoomUtils.getDate(startTime));
            data.put("start", BookingRoomUtils.getHoursAndMinutes(startTime));
            data.put("end", BookingRoomUtils.getHoursAndMinutes(endTime));

            map.put("data", data);
            return map;
        }

        if (request.getParameter("type").compareTo("delete") == 0)
        {
            int id = Integer.parseInt(request.getParameter("id"));

            if (reservationDao.deleteReservation(id))
            {
                map.put("status", messageSource.getMessage("bookRoom.delete.success", null, RequestContextUtils.getLocale(request)));
            }
            else
            {
                map.put("status", messageSource.getMessage("bookRoom.delete.error", null, RequestContextUtils.getLocale(request)));
            }

            return map;
        }

        if (request.getParameter("type").compareTo("timeline") == 0)
        {
            String date = request.getParameter("date") + " 00:00:00";
            log.debug("XML DATE=" + date);
            GregorianCalendar monthStart = BookingRoomUtils.getCalendar(date);
            monthStart.set(Calendar.DAY_OF_MONTH, 1);
            GregorianCalendar monthEnd = (GregorianCalendar) monthStart.clone();
            monthEnd.add(Calendar.MONTH, 1);
            monthEnd.add(Calendar.SECOND, -1);

            String xml = BookingRoomXmlUtils.formatReservationsList(reservationDao.getReservationsBetween(monthStart, monthEnd), personDao.getLoggedPerson());

            map.put("xmlContent", xml);
            return map;
        }

        throw new InvalidAttributeValueException("Attribute '" + request.getParameter("type") + "' is not allowed!");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception
    {

        ModelAndView mav = new ModelAndView(getSuccessView());
        return mav;
    }


    public HierarchicalMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public ReservationDao getReservationDao()
    {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao)
    {
        this.reservationDao = reservationDao;
    }

    public ResearchGroupDao getResearchGroupDao()
    {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao)
    {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonDao getPersonDao()
    {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao)
    {
        this.personDao = personDao;
    }
}
