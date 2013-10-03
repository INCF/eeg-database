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
 *   BookingRoomControllerTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import cz.zcu.kiv.eegdatabase.logic.AbstractControllerTest;
import cz.zcu.kiv.eegdatabase.mock.MockFactory;
import cz.zcu.kiv.eegdatabase.mock.SimplePersonDao;
import org.junit.Ignore;
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
