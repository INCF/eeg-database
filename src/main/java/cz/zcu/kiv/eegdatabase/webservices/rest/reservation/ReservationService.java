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
