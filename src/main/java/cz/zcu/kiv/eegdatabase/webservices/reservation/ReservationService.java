package cz.zcu.kiv.eegdatabase.webservices.reservation;

import cz.zcu.kiv.eegdatabase.webservices.reservation.wrappers.ReservationData;
import org.springframework.security.access.annotation.Secured;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Interface for Reservation RESTful webservice.
 *
 * @author Petr Miko
 */
@Path("reservation")
@WebService
@Produces("application/xml")
@Secured("IS_AUTHENTICATED_FULLY")
public interface ReservationService {

    @GET
    @Path("/all")
    public List<ReservationData> getAll();
}
