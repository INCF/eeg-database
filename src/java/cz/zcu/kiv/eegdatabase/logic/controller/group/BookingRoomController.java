package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

public class BookingRoomController extends SimpleFormController {
    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ReservationDao reservationDao;
    private HierarchicalMessageSource messageSource;

    public BookingRoomController() {
        setCommandClass(BookRoomCommand.class);
        setCommandName("bookRoomCommand");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object comm, BindException bindException) throws Exception {

        String status = null;
        String comment = null;
        try {
            BookRoomCommand command = (BookRoomCommand) comm;
            status = messageSource.getMessage("bookRoom.controllerMessages.status.fail", null, RequestContextUtils.getLocale(request));
            comment = messageSource.getMessage("bookRoom.controllerMessages.comment.error.unknown", null, RequestContextUtils.getLocale(request));

            Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
            String[] startTime = request.getParameter("startTime").split(":");
            String[] endTime = request.getParameter("endTime").split(":");
            String[] date = request.getParameter("date").split("/");
            int group = Integer.parseInt(request.getParameter("selectedGroup"));
            int repType = Integer.parseInt(request.getParameter("repType"));
            int repCount = Integer.parseInt(request.getParameter("repCount"));

            int day = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);
            int h = Integer.parseInt(startTime[0]);
            int m = Integer.parseInt(startTime[1]);

            Reservation res = new Reservation();

            Timestamp createTime = new Timestamp(new GregorianCalendar().getTimeInMillis());
            res.setCreationTime(createTime);
            res.setStartTime(new Timestamp(new GregorianCalendar(year, month - 1, day, h, m, 0).getTimeInMillis()));
            h = Integer.parseInt(endTime[0]);
            m = Integer.parseInt(endTime[1]);
            res.setEndTime(new Timestamp(new GregorianCalendar(year, month - 1, day, h, m, 0).getTimeInMillis()));

            res.setPerson(user);

            //searching for ResearchGroup
            ResearchGroup grp = getResearchGroup(group);
            res.setResearchGroup(grp);

            log.debug("Reservation has been created: " + ((res == null) ? "false" : "true"));
            reservationDao.create(res);

            if (repCount > 0) {
                comment = messageSource.getMessage("bookRoom.controllerMessages.comment.booked.multiple.part1", null, RequestContextUtils.getLocale(request));
                log.debug("RESERVATION Repetition count = " + repCount);
                log.debug("RESERVATION Repetition type = " + repType);

                int hS = Integer.parseInt(startTime[0]);
                int mS = Integer.parseInt(startTime[1]);
                int hE = Integer.parseInt(endTime[0]);
                int mE = Integer.parseInt(endTime[1]);

                comment += (day < 10 ? "0" : "") + day + "/" + (month < 10 ? "0" : "") + month + "/" + year + ", from " + (hS < 10 ? "0" : "") + hS + ":" + (mS == 0 ? "0" : "") + mS + " to " + (hE < 10 ? "0" : "") + hE + ":" + (mE == 0 ? "0" : "") + mE + "<br>";

                int weekNum = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

                log.debug("RESERVATION Current week = " + weekNum);

                GregorianCalendar nextS = new GregorianCalendar(year, month - 1, day, hS, mS, 0);
                GregorianCalendar nextE = new GregorianCalendar(year, month - 1, day, hE, mE, 0);

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

                    Reservation newReservation = new Reservation();
                    newReservation.setCreationTime(createTime);
                    newReservation.setStartTime(new Timestamp(nextS.getTimeInMillis()));
                    newReservation.setEndTime(new Timestamp(nextE.getTimeInMillis()));

                    newReservation.setPerson(user);
                    newReservation.setResearchGroup(grp);

                    reservationDao.create(newReservation);

                    String dayE = nextS.get(Calendar.DAY_OF_MONTH) + "";
                    if (dayE.length() == 1) dayE = "0" + dayE;
                    String monthE = (nextS.get(Calendar.MONTH) + 1) + "";
                    if (monthE.length() == 1) monthE = "0" + monthE;
                    comment += dayE + "/" + monthE + "/" + nextS.get(Calendar.YEAR) + ", from " + (hS < 10 ? "0" : "") + hS + ":" + (mS == 0 ? "0" : "") + mS + " to " + (hE < 10 ? "0" : "") + hE + ":" + (mE == 0 ? "0" : "") + mE + "<br>";
                }

                comment += String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.multiple.part2", null, RequestContextUtils.getLocale(request)), repCount + 1);
                //comment += " have been created! (totally " + (repCount + 1) + " reservations)";//+1 because we need count "original" reservation!
            } else {
                comment = String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.single", null, RequestContextUtils.getLocale(request)), command.getDateString(), command.getStartTimeString(), command.getEndTimeString());
                //comment = "A reservation to " + request.getParameter("date") + ", from " + request.getParameter("startTime") + " to " + request.getParameter("endTime") + " has been created!";
            }

            status = messageSource.getMessage("bookRoom.controllerMessages.status.ok", null, RequestContextUtils.getLocale(request));

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage() + "\n" + e.getStackTrace()[0].getFileName() + " at line " + e.getStackTrace()[0].getLineNumber(), e);

            status = messageSource.getMessage("bookRoom.controllerMessages.status.fail", null, RequestContextUtils.getLocale(request));
            log.info("After critical");
            comment = messageSource.getMessage("bookRoom.controllerMessages.comment.error.exception", null, RequestContextUtils.getLocale(request)) + " " + e.getMessage();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + "?status=" + status + "&comment=" + comment);

        return mav;

    }

    /**
     * Method to get chosen researchgroup by id.
     *
     * @param id ID of the researchgroup.
     * @return Chosen researchgroup.
     * @throws Exception When researchgroup with ID does not exist.
     */
    private ResearchGroup getResearchGroup(int id) throws Exception {
        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        Iterator it = user.getResearchGroupMemberships().iterator();
        while (it.hasNext()) {
            ResearchGroupMembership tmp = (ResearchGroupMembership) it.next();
            if (tmp.getResearchGroup().getResearchGroupId() == id)
                return tmp.getResearchGroup();
        }
        throw new Exception("ResearchGroup with id='" + id + "' was not found!");
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        map.put("defaultGroupId", defaultGroupId);
        map.put("researchGroupList", researchGroupList);

        return map;
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }


    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }
}
