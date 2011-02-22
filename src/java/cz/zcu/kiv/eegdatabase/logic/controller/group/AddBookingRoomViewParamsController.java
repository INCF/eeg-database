package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class AddBookingRoomViewParamsController
        extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());

    private ReservationDao reservationDao;
    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;
    private HierarchicalMessageSource messageSource;

    public AddBookingRoomViewParamsController() {
        setCommandClass(AddBookingRoomViewParamsCommand.class);
        setCommandName("addBookingRoomViewParams");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        log.debug("Preparing data for form");

        Map map = new HashMap<String, Object>();

        String[] startTime = request.getParameter("startTime").split(":");
        String[] endTime = request.getParameter("endTime").split(":");
        String[] date = request.getParameter("date").split("/");
        int group = Integer.parseInt(request.getParameter("group"));
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        int h = Integer.parseInt(startTime[0]);
        int m = Integer.parseInt(startTime[1]);

        GregorianCalendar cal = new GregorianCalendar(year, month - 1, day, 0, 0, 0);
        log.info("ORIG= " + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " 00:00:00");


        if (repCount > 0) {
            int hS = Integer.parseInt(startTime[0]);
            int mS = Integer.parseInt(startTime[1]);
            int hE = Integer.parseInt(endTime[0]);
            int mE = Integer.parseInt(endTime[1]);

            int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
            GregorianCalendar nextS = new GregorianCalendar(year, month - 1, day, hS, mS, 0);
            GregorianCalendar nextE = new GregorianCalendar(year, month - 1, day, hE, mE, 0);
            List coll = new ArrayList();
            for (int i = 0; i < repCount; i++) {
                int add = 0;
                if (repType == 0) add = 1;
                if ((repType == 1 && weekNum % 2 == 1) || (repType == 2 && weekNum % 2 == 0)) {
                    add = 2;
                }
                if ((repType == 1 && weekNum % 2 == 0) || (repType == 2 && weekNum % 2 == 1)) {
                    if (i == 0) add = 1;
                    else add = 2;
                }

                nextS.add(Calendar.WEEK_OF_YEAR, add);
                nextE.add(Calendar.WEEK_OF_YEAR, add);

                List nextRes = reservationDao.getReservationsBetween(nextS, nextE);
                if (nextRes.size() > 0) {
                    coll.addAll(nextRes);
                }
            }
            map.put("collisions", coll);
            map.put("collisionsCount", coll.size());
        }

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        GregorianCalendar weekStart = (GregorianCalendar) cal.clone();

        String startStr = weekStart.get(Calendar.DAY_OF_MONTH) + "-" + (weekStart.get(Calendar.MONTH) + 1) + "-" + weekStart.get(Calendar.YEAR) + " 00:00:00";
        log.info("START= " + startStr);

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        GregorianCalendar weekEnd = (GregorianCalendar) cal.clone();

        String endStr = weekEnd.get(Calendar.DAY_OF_MONTH) + "-" + (weekEnd.get(Calendar.MONTH) + 1) + "-" + weekEnd.get(Calendar.YEAR) + " 00:00:00";
        log.info("END= " + endStr);

        map.put("reservations", reservationDao.getReservationsBetween(weekStart, weekEnd));


        map.put("repCount", repCount);


        map.put("timerange", request.getParameter("date") + "; " + request.getParameter("startTime") + " - " + request.getParameter("endTime"));


        map.put("check", "[group=" + group + " startTime=" + startStr + " endTime=" + endStr + "]");

        log.debug("Returning map object");
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {

        ModelAndView mav = new ModelAndView(getSuccessView());
        return mav;
    }


    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
