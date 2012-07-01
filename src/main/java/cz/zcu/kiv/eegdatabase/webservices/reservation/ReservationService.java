package cz.zcu.kiv.eegdatabase.webservices.reservation;

import cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers.ResearchGroupData;
import cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers.ReservationData;
import org.springframework.security.access.annotation.Secured;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Interface for Reservation RESTful webservice.
 *
 * @author Petr Miko
 */
@WebService
@Produces("application/xml")
@Secured("IS_AUTHENTICATED_FULLY")
public interface ReservationService {

    @GET
    @Path("/{date}")
    public List<ReservationData> getToDate(@PathParam("date") String date) throws ReservationException;

    @GET
    @Path("/{fromDate}/{toDate}")
    public List<ReservationData> getFromToDate(@PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate) throws ReservationException;

    @GET
    @Path("/groups")
    public List<ResearchGroupData> getMyGroups() throws ReservationException;

    @PUT
    @Path("/{groupId}/{date}/{fromHour}/{toHour}")
    public Response create(@PathParam("groupId")int groupId, @PathParam("date") String date, @PathParam("fromHour") String fromHour, @PathParam("toHour") String toHour) throws ReservationException;

    @DELETE
    @Path("/{groupId}")
    public Response delete(@PathParam("groupId")int reservationId) throws ReservationException;
}
