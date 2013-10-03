/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   BookingRoomController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.group;

/**
 * @author Jenda Kolena
 */

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
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

public class BookingRoomController extends SimpleFormController
{
    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ReservationDao reservationDao;
    private HierarchicalMessageSource messageSource;

    private String status = null;
    private String comment = null;

    public BookingRoomController()
    {
        setCommandClass(BookRoomCommand.class);
        setCommandName("bookRoomCommand");
    }

    @Override
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object comm, BindException bindException) throws Exception
    {
        try
        {
            BookRoomCommand command = (BookRoomCommand) comm;
            status = messageSource.getMessage("bookRoom.controllerMessages.status.fail", null, RequestContextUtils.getLocale(request));
            comment = messageSource.getMessage("bookRoom.controllerMessages.comment.error.unknown", null, RequestContextUtils.getLocale(request));

            Person user = personDao.getLoggedPerson();
            int group = command.getSelectedGroup();
            int repType = command.getRepType();
            int repCount = command.getRepCount();
            String startStr = BookingRoomUtils.getHoursAndMinutes(command.getStartTimeCal());
            String endStr = BookingRoomUtils.getHoursAndMinutes(command.getEndTimeCal());

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

            if (repCount > 0)
            {
                comment = messageSource.getMessage("bookRoom.controllerMessages.comment.booked.multiple.part1", null, RequestContextUtils.getLocale(request));
                comment += command.getDate() + ", from " + startStr + " to " + endStr + "<br>\n";

                GregorianCalendar nextS = command.getStartTimeCal();
                GregorianCalendar nextE = command.getEndTimeCal();

                for (int i = 0; i < repCount; i++)
                {
                    //shift of dates
                    int add = BookingRoomUtils.getWeeksAddCount(repType, i);
                    nextS.add(Calendar.WEEK_OF_YEAR, add);
                    nextE.add(Calendar.WEEK_OF_YEAR, add);
                    Reservation newReservation = new Reservation();
                    newReservation.setCreationTime(createTime);
                    newReservation.setStartTime(new Timestamp(nextS.getTimeInMillis()));
                    newReservation.setEndTime(new Timestamp(nextE.getTimeInMillis()));
                    newReservation.setPerson(user);
                    newReservation.setResearchGroup(grp);
                    reservationDao.create(newReservation);

                    comment += BookingRoomUtils.getDate(nextS) + ", from " + BookingRoomUtils.getHoursAndMinutes(nextS) + " to " + BookingRoomUtils.getHoursAndMinutes(nextE) + "<br>\n";
                }

                comment += String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.multiple.part2", null, RequestContextUtils.getLocale(request)), repCount + 1);//+1 because we need count "original" reservation!
            }
            else
            {
                comment = String.format(messageSource.getMessage("bookRoom.controllerMessages.comment.booked.single", null, RequestContextUtils.getLocale(request)), command.getDate(), startStr, endStr);
            }

            status = messageSource.getMessage("bookRoom.controllerMessages.status.ok", null, RequestContextUtils.getLocale(request));
        } catch (Exception e)
        {
            log.error("Exception: " + e.getMessage() + "\n" + e.getStackTrace()[0].getFileName() + " at line " + e.getStackTrace()[0].getLineNumber(), e);

            status = messageSource.getMessage("bookRoom.controllerMessages.status.fail", null, RequestContextUtils.getLocale(request));
            comment = messageSource.getMessage("bookRoom.controllerMessages.comment.error.exception", null, RequestContextUtils.getLocale(request)) + " " + e.getMessage();
        }

        log.debug("Returning MAV" + " with status=" + status + "&comment=" + comment);
        ModelAndView mav = new ModelAndView(getSuccessView()/* + "?status=" + status + "&comment=" + comment*/);

        return mav;
    }

    /**
     * Method to get chosen researchgroup by id.
     *
     * @param id ID of the researchgroup.
     * @return Chosen researchgroup.
     * @throws Exception When researchgroup with ID does not exist.
     */
    private ResearchGroup getResearchGroup(int id) throws Exception
    {
        Person user = personDao.getLoggedPerson();
        List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereMember(user);
        groups.addAll(researchGroupDao.getResearchGroupsWhereOwner(user));
        groups.addAll(researchGroupDao.getResearchGroupsWhereUserIsGroupAdmin(user));

        for (ResearchGroup group : groups)
            if (group.getResearchGroupId() == id) return group;

        throw new Exception("ResearchGroup with id='" + id + "' was not found!");
    }


    @Override
    public Map referenceData(HttpServletRequest request) throws Exception
    {
        Map map = new HashMap<String, Object>();
        List<ResearchGroup> researchGroupList =
                researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        map.put("defaultGroupId", defaultGroupId);
        map.put("researchGroupList", researchGroupList);

        //if status or comment is prepared, send then delete them
        if (status != null)
        {
            map.put("status", new String(status));
            status = null;
        }
        if (comment != null)
        {
            map.put("comment", new String(comment));
            comment = null;
        }

        GroupMultiController.setPermissionToRequestGroupRole(map, personDao.getLoggedPerson());
        return map;
    }

    public HierarchicalMessageSource getMessageSource()
    {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public PersonDao getPersonDao()
    {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao)
    {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao()
    {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao)
    {
        this.researchGroupDao = researchGroupDao;
    }


    public ReservationDao getReservationDao()
    {
        return reservationDao;
    }

    public void setReservationDao(ReservationDao reservationDao)
    {
        this.reservationDao = reservationDao;
    }
}
