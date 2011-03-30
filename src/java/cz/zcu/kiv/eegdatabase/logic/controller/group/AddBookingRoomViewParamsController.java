package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AddBookingRoomViewParamsController extends SimpleFormController
{
    private Log log = LogFactory.getLog(getClass());
    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private HierarchicalMessageSource messageSource;

    public AddBookingRoomViewParamsController()
    {
        setCommandClass(BookRoomCommand.class);
        setCommandName("bookRoomCommand");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception
    {
        Map map = new HashMap<String, Object>();

        int group = Integer.parseInt(request.getParameter("group"));
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        String date = request.getParameter("date");
        String startStr = request.getParameter("startTime");
        String endStr = request.getParameter("endTime");

        GregorianCalendar cal = BookingRoomUtils.getCalendar(startStr);

        //reservations in not visible time period
        if (repCount > 0)
        {
            int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
            GregorianCalendar nextS = BookingRoomUtils.getCalendar(startStr);
            GregorianCalendar nextE = BookingRoomUtils.getCalendar(endStr);
            List coll = new ArrayList();
            for (int i = 0; i < repCount; i++)
            {
                int add = BookingRoomUtils.getWeeksAddCount(repType, i);
                nextS.add(Calendar.WEEK_OF_YEAR, add);
                nextE.add(Calendar.WEEK_OF_YEAR, add);

                List nextRes = reservationDao.getReservationsBetween(nextS, nextE);
                if (nextRes.size() > 0)
                {
                    coll.addAll(nextRes);
                }
            }
            map.put("collisions", coll);
            map.put("collisionsCount", coll.size());
        }

        //reservations in currently visible time period
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        GregorianCalendar weekStart = BookingRoomUtils.getCalendar(BookingRoomUtils.getDate(cal) + " 00:00:00");

        GregorianCalendar weekEnd = (GregorianCalendar) weekStart.clone();
        weekEnd.add(Calendar.DAY_OF_YEAR, 7);
        weekEnd.add(Calendar.SECOND, -1);

        map.put("reservations", reservationDao.getReservationsBetween(weekStart, weekEnd));
        map.put("timerange", date + " " + BookingRoomUtils.getHoursAndMinutes(startStr) + " - " + BookingRoomUtils.getHoursAndMinutes(endStr));
        map.put("displayed", String.format(messageSource.getMessage("bookRoom.displayed", null, RequestContextUtils.getLocale(request)), BookingRoomUtils.getDate(weekStart), BookingRoomUtils.getDate(weekEnd)/*+" "+BookingRoomUtils.getTime(weekEnd)*/));

        /*
        -- JSP can get this from params object --
        map.put("repCount", repCount);
        map.put("repType", repType);
        map.put("group", group);
        map.put("date", date);*/
        map.put("startTime", BookingRoomUtils.getHoursAndMinutes(startStr).replaceAll(":", ""));
        map.put("endTime", BookingRoomUtils.getHoursAndMinutes(endStr).replaceAll(":", ""));
        map.put("loggedUser", personDao.getLoggedPerson());

        return map;
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
