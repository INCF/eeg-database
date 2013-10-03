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
 *   ReservationDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Reservation;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Data Access Object for accessing Reservation entities. The interface will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jan Kolena
 */
public interface ReservationDao extends GenericDao<Reservation, Integer>
{
    public int createChecked(Reservation newInstance) throws DaoException;

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end);

    public List<Reservation> getReservationsBetween(GregorianCalendar start, GregorianCalendar end, String date, int group);

    public Reservation getReservationById(int id);

    public boolean deleteReservation(int id);
}
