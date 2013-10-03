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
 *   ReservationService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.reservation;


import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationData;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationDataList;

/**
 * Reservation service interface.
 *
 * @author Petr Miko
 */

public interface ReservationService {

    /**
     * Getter of reservations created to specified date.
     *
     * @param date date string in dd.MM.yyyy format
     * @return reservations to specified date
     * @throws RestServiceException error while obtaining reservations
     */
    public ReservationDataList getToDate(String date) throws RestServiceException;

    /**
     * Getter of reservations created from specified date to other specified date.
     *
     * @param fromDate start date string in dd.MM.yyyy format
     * @param toDate   end date string in dd.MM.yyyy format
     * @return reservations between specified dates
     * @throws RestServiceException error while obtaining reservations
     */
    public ReservationDataList getFromToDate(String fromDate, String toDate) throws RestServiceException;

    /**
     * Method for creating new reservation on eeg base.
     *
     * @param reservationData reservation information
     * @return complete information about created reservation (including identifier)
     * @throws RestServiceException error while creating reservation
     */
    public ReservationData create(ReservationData reservationData) throws RestServiceException;

    /**
     * Method for removing existing reservation.
     *
     * @param reservationId identifier of reservation to be removed
     * @throws RestServiceException error while removing reservation
     */
    public void delete(int reservationId) throws RestServiceException;

}
