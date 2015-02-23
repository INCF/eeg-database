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
 *   ReservationDaoTest.java, 2014/07/08 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.testng.Assert.*;


/**
 * Created by stebjan on 8.7.14.
 */
public class ReservationDaoTest extends AbstractDataAccessTest {
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ReservationDao reservationDao;

    private ResearchGroup researchGroup;
    private Person person;
    private Reservation reservation;

    private final long TWO_HOURS_TO_MILLIS = 1000 * 60 * 60 * 2;
    private final String DATE = "01/01/2010";

    @BeforeMethod(groups = "unit")
    public void setUp() throws ParseException {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);


        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);

        reservation = createReservation(getDate(DATE));

    }

    @Test(groups = "unit")
    public void testCreateReservation() {
        int countBefore = reservationDao.getCountRecords();
        reservationDao.create(reservation);
        assertEquals(countBefore + 1, reservationDao.getCountRecords());
    }

    @Test(groups = "unit")
    public void testEndDateChecking() throws ParseException {
        int countBefore = reservationDao.getCountRecords();
        reservationDao.create(reservation);
        reservation = createReservation(new Timestamp(getDate(DATE).getTime() + TWO_HOURS_TO_MILLIS));
        reservation.setEndTime(getDate(DATE));
        try {
            reservationDao.createChecked(reservation);
        } catch (Exception ex) {
            assertTrue(ex instanceof DaoException);
        } finally {
            assertEquals(countBefore + 1, reservationDao.getCountRecords());
        }
    }

    @Test(groups = "unit")
    public void testExistingReservation() {
        int countBefore = reservationDao.getCountRecords();
        reservationDao.create(reservation);
        Reservation reservation2 = createReservation
                (reservation.getStartTime());
        try {
            reservationDao.createChecked(reservation2);
        } catch (Exception ex) {
            assertTrue(ex instanceof DaoException);
        } finally {
            assertEquals(countBefore + 1, reservationDao.getCountRecords());
        }

    }
    @Test(groups = "unit")
    public void testGetReservationBetween() throws ParseException {
        int countBefore = reservationDao.getCountRecords();
        try {
        reservationDao.createChecked(reservation);
        reservation = createReservation(new Timestamp((reservation.getEndTime().getTime() + TWO_HOURS_TO_MILLIS)));
        reservationDao.createChecked(reservation);
        reservation = createReservation(new Timestamp(0));

        reservationDao.createChecked(reservation);
        } catch (Exception ignored) {

        }   finally {
            assertEquals(countBefore + 3, reservationDao.getCountRecords());
            GregorianCalendar calStart = getCalendar("01/01/2010 00:00:01");
            //new GregorianCalendar(2000, 0, 1);
            GregorianCalendar calEnd = getCalendar("01/01/2010 23:59:29");
                    //new GregorianCalendar(2000, 0, 2);
            List<Reservation> list = reservationDao.getReservationsBetween(calStart, calEnd);
            assertEquals(countBefore + 2, list.size());
        }

    }

    private Reservation createReservation(Timestamp startTime) {
        Reservation reservation = new Reservation();
        reservation.setResearchGroup(researchGroup);
        reservation.setPerson(person);
        reservation.setCreationTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStartTime(startTime);
        reservation.setEndTime(new Timestamp(startTime.getTime() + TWO_HOURS_TO_MILLIS));
        return reservation;
    }

    private Timestamp getDate(String stringDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(stringDate);
        long time = date.getTime();
        return new Timestamp(time);
    }

    private GregorianCalendar getCalendar(String stringDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateFormat.parse(stringDate));
        return cal;
    }
}
