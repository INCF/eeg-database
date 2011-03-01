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
            int group = command.getSelectedGroup();
            int repType = command.getRepType();
            int repCount = command.getRepCount();

            Reservation res = new Reservation();

            Timestamp createTime = new Timestamp(new GregorianCalendar().getTimeInMillis());
            res.setCreationTime(createTime);
            res.setStartTime(command.getStartTimeTimestamp());
            res.setEndTime(command.getEndTimeTimestamp());

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

                comment += command.getDate() + ", from " + BookRoomCommand.getTime(command.getStartTime()) + " to " + BookRoomCommand.getTime(command.getEndTime()) + "<br>\n";

                int weekNum = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

                log.debug("RESERVATION Current week = " + weekNum);

                GregorianCalendar nextS = command.getStartTime();
                GregorianCalendar nextE = command.getEndTime();

                for (int i = 0; i < repCount; i++) {
                    //shift of dates
                    setRepetitionDate(repType, i, nextS, nextE);

                    Reservation newReservation = new Reservation();
                    newReservation.setCreationTime(createTime);
                    newReservation.setStartTime(new Timestamp(nextS.getTimeInMillis()));
                    newReservation.setEndTime(new Timestamp(nextE.getTimeInMillis()));

                    newReservation.setPerson(user);
                    newReservation.setResearchGroup(grp);

                    reservationDao.create(newReservation);

                    comment += BookRoomCommand.getDate(nextS) + ", from " + BookRoomCommand.getTime(nextS) + " to " + BookRoomCommand.getTime(nextE) + "<br>\n";
                }

                comment += String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.multiple.part2", null, RequestContextUtils.getLocale(request)), repCount + 1);//+1 because we need count "original" reservation!
            } else {
                comment = String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.single", null, RequestContextUtils.getLocale(request)), command.getDate(), command.getStartTimeString(), command.getEndTimeString());
            }
            status = messageSource.getMessage("bookRoom.controllerMessages.status.ok", null, RequestContextUtils.getLocale(request));
        } catch (Exception e) {
            //log.error("Exception was thrown: ",e);
            log.error("Exception: " + e.getMessage() + "\n" + e.getStackTrace()[0].getFileName() + " at line " + e.getStackTrace()[0].getLineNumber(), e);

            status = messageSource.getMessage("bookRoom.controllerMessages.status.fail", null, RequestContextUtils.getLocale(request));
            comment = messageSource.getMessage("bookRoom.controllerMessages.comment.error.exception", null, RequestContextUtils.getLocale(request)) + " " + e.getMessage();
        }

        log.debug("Returning MAV" + " with status=" + status + "&comment=" + comment);
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

    /**
     * Sets dates of next repetition.
     *
     * @param repType   Type of repetition.
     * @param repIndex  Index of repetition (defacto FOR cycle).
     * @param startTime GregorianCalendar, which will be shifted.
     * @param endTime   GregorianCalendar, which will be shifted.
     */
    public static void setRepetitionDate(int repType, int repIndex, GregorianCalendar startTime, GregorianCalendar endTime) {
        int weekNum = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);

        int add = 0;
        if (repType == 0) add = 1;
        if ((repType == 1 && weekNum % 2 == 1) || (repType == 2 && weekNum % 2 == 0)) {
            add = 2;
        }
        if ((repType == 1 && weekNum % 2 == 0) || (repType == 2 && weekNum % 2 == 1)) {
            if (repIndex == 0) add = 1;
            else add = 2;
        }

        startTime.add(Calendar.WEEK_OF_YEAR, add);
        endTime.add(Calendar.WEEK_OF_YEAR, add);
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
