package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ExperimentData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.ResearchGroupDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Implementation of user service
 *
 * @author Petr Miko
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/user")
public class UserServiceController {

    private final static Log log = LogFactory.getLog(UserServiceController.class);
    @Autowired
    UserService service;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UserInfo login() {
        return service.login();
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResearchGroupDataList getMyGroups() throws RestServiceException {
        return service.getMyGroups();
    }

    @RequestMapping(value = "/experiments", method = RequestMethod.GET)
    public List<ExperimentData> getMyExperiments() throws RestServiceException {
        return service.getMyExperiments();
    }

    @ExceptionHandler(RestServiceException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "REST service error occurred")
    public String handleRSException(RestServiceException ex) {
        log.error(ex);
        return ex.getMessage();
    }
}
