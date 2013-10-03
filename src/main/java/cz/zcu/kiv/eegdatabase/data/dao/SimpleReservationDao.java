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
 *   SimpleReservationDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;
import cz.zcu.kiv.eegdatabase.logic.util.BookingRoomUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Implementation of DAO for accessing Reservation entities. This class will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public class SimpleReservationDao
        extends SimpleGenericDao<Reservation, Integer>
        implements ReservationDao {

    public SimpleReservationDao() {
        super(Reservation.class);
    }

    public int createChecked(Reservation newInstance) throws DaoException {

        if (newInstance.getStartTime().getTime() >= newInstance.getEndTime().getTime()) {
            throw new DaoException("Time of start cannot be lower than or equal to time of end!");
        }
        Criteria overlapCriteria = getSession().createCriteria(Reservation.class);
        //start overlap
        LogicalExpression startOverlap = Restrictions.and(
                Restrictions.and(
                        Restrictions.ge("startTime", newInstance.getStartTime()),
                        Restrictions.ge("endTime", newInstance.getEndTime())),
                Restrictions.le("startTime", newInstance.getEndTime())
        );
        //end overlap
        LogicalExpression endOverlap = Restrictions.and(
                Restrictions.and(
                        Restrictions.le("startTime", newInstance.getStartTime()),
                        Restrictions.le("endTime", newInstance.getEndTime())),
                Restrictions.ge("endTime", newInstance.getStartTime())
        );
        //include overlap
        LogicalExpression inOverlap = Restrictions.and(Restrictions.le("startTime", newInstance.getStartTime()),
                Restrictions.ge("endTime", newInstance.getEndTime()));

        //complete overlap
        LogicalExpression completeOverlap = Restrictions.and(Restrictions.ge("startTime", newInstance.getStartTime()),
                Restrictions.le("endTime", newInstance.getEndTime()));

        overlapCriteria.add(Restrictions.or(Restrictions.or(inOverlap, completeOverlap), Restrictions.or(startOverlap, endOverlap)));

        //if some overlap was found, it is an error
        if (overlapCriteria.list().size() > 0) {
            throw new DaoException("Reservation could not be created due to existing records within the time range.");
        }

        return super.create(newInstance);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end) {
        return getReservationsBetween(start, end, "", 0);
    }

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String date, int group) {
        if (date != null && date.compareTo("") != 0) {//filter of date
            start = BookingRoomUtils.getCalendar(date + " 00:00:00");
            end = BookingRoomUtils.getCalendar(date + " 23:59:59");
        }

        String filterGroup = "";
        if (group > 0) {
            filterGroup = " AND reservation.researchGroup = " + group;
        }

        String hqlQuery = "from Reservation reservation where reservation.startTime < (:endtime) AND reservation.endTime > (:starttime)" + filterGroup + " order by reservation.startTime";
        Session session = getSession();
        return session.createQuery(hqlQuery).setTimestamp("starttime", start.getTime()).setTimestamp("endtime", end.getTime()).list();
    }

    public Reservation getReservationById(int id) {
        String hqlQuery = "from Reservation reservation where reservation.reservationId = :id";
        Session session = getSession();
        List res = session.createQuery(hqlQuery).setInteger("id", id).list();
        return res.size() == 1 ? (Reservation) res.get(0) : null;
    }

    public boolean deleteReservation(int id) {
        String hqlQuery = "delete from Reservation reservation where reservation.reservationId = :id";
        Session session = getSession();
        return session.createQuery(hqlQuery).setInteger("id", id).executeUpdate() == 1;
    }
}
