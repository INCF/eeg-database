package cz.zcu.kiv.eegdatabase.webservices.rest.reservation;

import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ResearchGroupData;
import cz.zcu.kiv.eegdatabase.webservices.rest.reservation.wrappers.ReservationData;
import org.springframework.security.access.annotation.Secured;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Interface for Reservation RESTful webservice.
 *
 * @author Petr Miko
 */
@WebService
@Secured("IS_AUTHENTICATED_FULLY")
@Path("/reservation")
public interface ReservationService {

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/{date}")
    public List<ReservationData> getToDate(@PathParam("date") String date) throws ReservationException;

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/{fromDate}/{toDate}")
    public List<ReservationData> getFromToDate(@PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate) throws ReservationException;

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("/groups")
    public List<ResearchGroupData> getMyGroups() throws ReservationException;

    @POST
    @Produces({MediaType.APPLICATION_XML})
    @Path("/")
    public Response create(ReservationData reservationData) throws ReservationException;

    @DELETE
    @Consumes({MediaType.APPLICATION_XML})
    @Path("/")
    public Response delete(ReservationData data) throws ReservationException;
}
