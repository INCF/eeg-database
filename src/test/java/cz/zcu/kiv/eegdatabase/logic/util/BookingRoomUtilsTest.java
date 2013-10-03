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
 *   BookingRoomUtilsTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.util;

import cz.zcu.kiv.eegdatabase.logic.AbstractControllerTest;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public class BookingRoomUtilsTest extends AbstractControllerTest
{
    private String date = "02/05/11";
    private String time = "18:00:00";

    @Test
    public void testGetCalendar() throws Exception
    {
        GregorianCalendar cal = BookingRoomUtils.getCalendar(date + " " + time);
        compare(cal, date, time);
    }

    @Test
    public void testGetDate() throws Exception
    {
        GregorianCalendar cal = new GregorianCalendar();
        String date = BookingRoomUtils.getDate(cal);
        String time = BookingRoomUtils.getTime(cal);

        compare(cal, date, time);
    }

    @Test
    public void testGetHoursAndMinutes() throws Exception
    {
        GregorianCalendar cal = new GregorianCalendar();
        String time = BookingRoomUtils.getTime(cal);
        assertEquals(time, BookingRoomUtils.getHoursAndMinutes(cal) + ":" + time.split(":")[2]);
    }

    private void compare(GregorianCalendar cal, String date, String time)
    {
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), Integer.parseInt(date.split("/")[0]));
        assertEquals(cal.get(Calendar.MONTH) + 1, Integer.parseInt(date.split("/")[1]));
        assertEquals(cal.get(Calendar.YEAR), Integer.parseInt("20" + date.split("/")[2]));

        assertEquals(cal.get(Calendar.HOUR), Integer.parseInt(time.split(":")[0]) % 12);
        assertEquals(cal.get(Calendar.MINUTE), Integer.parseInt(time.split(":")[1]));
        assertEquals(cal.get(Calendar.SECOND), Integer.parseInt(time.split(":")[2]));
    }

}
