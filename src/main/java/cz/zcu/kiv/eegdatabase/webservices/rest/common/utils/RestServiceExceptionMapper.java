package cz.zcu.kiv.eegdatabase.webservices.rest.common.utils;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for RestServiceException.
 *
 * @author Petr Miko
 */
@Provider
public class RestServiceExceptionMapper implements ExceptionMapper<RestServiceException> {

    @Override
    public Response toResponse(RestServiceException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
