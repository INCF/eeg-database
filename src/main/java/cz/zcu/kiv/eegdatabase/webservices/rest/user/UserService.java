package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.springframework.security.access.annotation.Secured;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Service for user information purposes.
 *
 * @author Petr Miko
 */
@WebService
@Secured("IS_AUTHENTICATED_FULLY")
@Path("/user")
public interface UserService {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/login")
    public UserInfo login();
}
