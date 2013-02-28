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

    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public ReservationDataList getToDate(@PathVariable String date) throws RestServiceException {
        return service.getToDate(date);
    }

    @RequestMapping(value = "/{fromDate}/{toDate}", method = RequestMethod.GET)
    public ReservationDataList getFromToDate(@PathVariable String fromDate, @PathVariable String toDate) throws RestServiceException {
        return service.getFromToDate(fromDate, toDate);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ReservationData create(@RequestBody ReservationData reservationData) throws RestServiceException {
        return service.create(reservationData);
    }

    @RequestMapping(value = "/{reservationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int reservationId) throws RestServiceException {
        service.delete(reservationId);
    }

    @ExceptionHandler(RestServiceException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "REST service error occurred")
    public String handleRSException(RestServiceException ex) {
        log.error(ex);
        return ex.getMessage();
    }
}
