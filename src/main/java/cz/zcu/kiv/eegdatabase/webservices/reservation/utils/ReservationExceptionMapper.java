package cz.zcu.kiv.eegdatabase.webservices.reservation.utils;

import cz.zcu.kiv.eegdatabase.webservices.reservation.ReservationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for ReservationException.
 *
 * @author Petr Miko
 */
@Provider
public class ReservationExceptionMapper implements ExceptionMapper<ReservationException> {

    @Override
    public Response toResponse(ReservationException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
