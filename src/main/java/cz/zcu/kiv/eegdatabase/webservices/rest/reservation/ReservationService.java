package cz.zcu.kiv.eegdatabase.webservices.rest.reservation;


import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationData;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationDataList;

/**
 * @author Petr Miko
 *         Date: 10.2.13
 */

public interface ReservationService {

    public ReservationDataList getToDate(String date) throws RestServiceException;

    public ReservationDataList getFromToDate(String fromDate, String toDate) throws RestServiceException;

    public ReservationData create(ReservationData reservationData) throws RestServiceException;

    public void delete(int reservationId) throws RestServiceException;

}
