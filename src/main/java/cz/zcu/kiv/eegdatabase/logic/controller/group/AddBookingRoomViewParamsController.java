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
 *   AddBookingRoomViewParamsController.java, 2013/10/02 00:01 Jakub Rinkes
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
    public Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception
    {
        Map map = new HashMap<String, Object>();

        int filterGroup = Integer.parseInt(request.getParameter("filterGroup"));
        String filterDate = request.getParameter("filterDate");
        if (filterDate.compareTo("") == 0) filterDate = null;
        int repType = Integer.parseInt(request.getParameter("repType"));
        int repCount = Integer.parseInt(request.getParameter("repCount"));

        String date = request.getParameter("date");
        String startStr = request.getParameter("startTime");
        String endStr = request.getParameter("endTime");

        GregorianCalendar cal = BookingRoomUtils.getCalendar(startStr);
        boolean collisions = false;

        //reservations in not visible time period
        if (repCount > 0)
        {
            GregorianCalendar nextS = BookingRoomUtils.getCalendar(startStr);
            GregorianCalendar nextE = BookingRoomUtils.getCalendar(endStr);

            List<Reservation> coll = BookingRoomUtils.getCollisions(reservationDao, repCount, repType, nextS, nextE);

            map.put("collisionsInNext", coll);
            map.put("collisionsInNextCount", coll.size());
            if (coll.size() > 0) collisions = true;
        }

        //reservations in currently visible time period
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        GregorianCalendar weekStart = BookingRoomUtils.getCalendar(BookingRoomUtils.getDate(cal) + " 00:00:00");

        GregorianCalendar weekEnd = (GregorianCalendar) weekStart.clone();
        weekEnd.add(Calendar.DAY_OF_YEAR, 7);
        weekEnd.add(Calendar.SECOND, -1);

        map.put("reservations", reservationDao.getReservationsBetween(weekStart, weekEnd, filterDate, filterGroup));
        map.put("timerange", date + " " + BookingRoomUtils.getHoursAndMinutes(startStr) + " - " + BookingRoomUtils.getHoursAndMinutes(endStr));

        String displayed = String.format(messageSource.getMessage("bookRoom.displayed.week", null, RequestContextUtils.getLocale(request)), BookingRoomUtils.getDate(weekStart), BookingRoomUtils.getDate(weekEnd));

        boolean filtered = false;

        if (filterDate != null)
        {//filter of date
            filtered = true;
            displayed = messageSource.getMessage("bookRoom.displayed.day", null, RequestContextUtils.getLocale(request)) + " " + filterDate;
        }

        if (filterGroup > 0)
        {
            filtered = true;
            displayed += (filterDate == null ? "," : " and") + " " + messageSource.getMessage("bookRoom.displayed.group", null, RequestContextUtils.getLocale(request)) + " " + getResearchGroup(filterGroup).getTitle();
        }

        if (filtered)
        {//we must verify that there are no reservations in selected range
            GregorianCalendar start = BookingRoomUtils.getCalendar(startStr);
            GregorianCalendar end = BookingRoomUtils.getCalendar(endStr);
            List<Reservation> coll = reservationDao.getReservationsBetween(start, end);
            if (coll.size() > 0)
            {
                //if the collision exists
                collisions = true;
                map.put("collisions", coll);
                map.put("collisionsCount", coll.size());
            }
        }

        map.put("displayed", displayed);

        map.put("collisionsExist", (collisions) ? "1" : "0");


        /*
       -- JSP can get this from params object --
       map.put("repCount", repCount);
       map.put("repType", repType);
       map.put("group", group);
       map.put("date", date);*/
        map.put("startTime", BookingRoomUtils.getHoursAndMinutes(startStr).replaceAll(":", ""));
        map.put("endTime", BookingRoomUtils.getHoursAndMinutes(endStr).replaceAll(":", ""));
        map.put("loggedUser", personDao.getLoggedPerson());
        GroupMultiController.setPermissionToRequestGroupRole(map, personDao.getLoggedPerson());

        return map;
    }

    /**
     * Method to get chosen researchgroup by id.
     *
     * @param id ID of the researchgroup.
     * @return Chosen researchgroup.
     * @throws Exception When researchgroup with ID does not exist.
     */
    public ResearchGroup getResearchGroup(int id) throws Exception
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
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception
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
