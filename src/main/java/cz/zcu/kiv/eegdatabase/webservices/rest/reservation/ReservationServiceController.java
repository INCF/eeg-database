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
 *   ReservationServiceController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.reservation;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationData;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationDataList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for mapping REST requests upon reservation system.
 *
 * @author Petr Miko
 */

@SuppressWarnings("unchecked")
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/reservation")
public class ReservationServiceController {

    private final static Log log = LogFactory.getLog(ReservationServiceController.class);
    @Autowired
    ReservationService service;

    /**
     * Getter of reservations created to specified date.
     *
     * @param date date string in dd.MM.yyyy format
     * @return reservations to specified date
     * @throws RestServiceException error while obtaining reservations
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public ReservationDataList getToDate(@PathVariable String date) throws RestServiceException {
        return service.getToDate(date);
    }

    /**
     * Getter of reservations created from specified date to other specified date.
     *
     * @param fromDate start date string in dd.MM.yyyy format
     * @param toDate   end date string in dd.MM.yyyy format
     * @return reservations between specified dates
     * @throws RestServiceException error while obtaining reservations
     */
    @RequestMapping(value = "/{fromDate}/{toDate}", method = RequestMethod.GET)
    public ReservationDataList getFromToDate(@PathVariable String fromDate, @PathVariable String toDate) throws RestServiceException {
        return service.getFromToDate(fromDate, toDate);
    }

    /**
     * Method for creating new reservation on eeg base.
     *
     * @param reservationData reservation information
     * @return complete information about created reservation (including identifier)
     * @throws RestServiceException error while creating reservation
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ReservationData create(@RequestBody ReservationData reservationData) throws RestServiceException {
        return service.create(reservationData);
    }

    /**
     * Method for removing existing reservation.
     *
     * @param reservationId identifier of reservation to be removed
     * @throws RestServiceException error while removing reservation
     */
    @RequestMapping(value = "/{reservationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int reservationId) throws RestServiceException {
        service.delete(reservationId);
    }

    /**
     * Exception handler for RestServiceException.class.
     * Writes exception message into HTTP response.
     *
     * @param ex       exception body
     * @param response HTTP response
     * @throws IOException error while writing error into response
     */
    @ExceptionHandler(RestServiceException.class)
    public void handleRSException(RestServiceException ex, HttpServletResponse response) throws IOException {
        log.error(ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
