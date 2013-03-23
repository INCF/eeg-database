package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ConfirmPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for handling REST requests on user service.
 * Requires fully authenticated access.
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
    @Value("${app.domain}")
    private String domain;

    /**
     * Method for creating new person/user with auto-generated password.
     *
     * @param request incoming http request
     * @param person person information
     * @return basic user/person information
     * @throws RestServiceException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo create(HttpServletRequest request, @RequestBody PersonData person) throws RestServiceException {

        String url = "http://" + domain + "/registration-confirm?" + ConfirmPage.CONFIRM_ACTIVATION + "=";

        return service.create(url, person, RequestContextUtils.getLocale(request));
    }

    /**
     * Test method for verifying user's credentials.
     *
     * @return basic user/person information
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UserInfo login() {
        return service.login();
    }

    /**
     * Exception handler for RestServiceException throwable type.
     * @param ex cause's throwable
     * @param response servlet response
     * @throws IOException error during sending HTTP error to response
     */
    @ExceptionHandler(RestServiceException.class)
    public void handleException(RestServiceException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        log.error(ex);
    }

    /**
     * Exception handler for RestNotFoundException throwable type.
     * @param ex cause's throwable
     * @param response servlet response
     * @throws IOException error during sending HTTP error to response
     */
    @ExceptionHandler(RestNotFoundException.class)
    public void handleException(RestNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        log.error(ex);
    }
}
