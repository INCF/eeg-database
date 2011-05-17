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

        int group = Integer.parseInt(request.getParameter("group"));
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        GregorianCalendar cal = BookRoomCommand.getCalendar(request.getParameter("startTime"));
        log.info("ORIG= " + request.getParameter("startTime") + " - " + request.getParameter("EndTime"));


        if (repCount > 0) {
            int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
            GregorianCalendar nextS = BookRoomCommand.getCalendar(request.getParameter("startTime"));
            GregorianCalendar nextE = BookRoomCommand.getCalendar(request.getParameter("endTime"));
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

        String startStr = request.getParameter("startTime");
        log.info("START= " + startStr);

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        GregorianCalendar weekEnd = (GregorianCalendar) cal.clone();

        String endStr = request.getParameter("endTime");
        log.info("END= " + endStr);

        map.put("reservations", reservationDao.getReservationsBetween(weekStart, weekEnd));
        map.put("repCount", repCount);
        map.put("timerange", request.getParameter("startTime") + " - " + request.getParameter("endTime").split(" ")[1]);
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
