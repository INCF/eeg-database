package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
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
    @Value("${app.domain}")
    private String domain;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo create(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name,
                           @RequestParam("surname") String surname, @RequestParam("gender") String gender,
                           @RequestParam("birthday") String birthday, @RequestParam("email") String email,
                           @RequestParam(value = "phone", required = false) String phoneNumber,
                           @RequestParam(value = "note", required = false) String note,
                           @RequestParam(value = "laterality", required = false) String laterality,
                           @RequestParam(value = "educationLevel", required = false) Integer educationLevel) throws RestServiceException {
        AddPersonCommand command = new AddPersonCommand();
        command.setGivenname(name);
        command.setSurname(surname);
        command.setDateOfBirth(birthday);
        command.setGender(gender);
        command.setEmail(email);
        command.setPhoneNumber(phoneNumber);
        command.setNote(note);
        command.setLaterality(laterality);
        if (educationLevel != null)
            command.setEducationLevel(educationLevel);

        String url = "http://" + domain + "/registration-confirm?" + ConfirmPage.CONFIRM_ACTIVATION + "=";

        return service.create(url, command, RequestContextUtils.getLocale(request));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UserInfo login() {
        return service.login();
    }

    @ExceptionHandler(RestServiceException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "REST service error occurred")
    public String handleRSException(RestServiceException ex) {
        log.error(ex);
        return ex.getMessage();
    }
}
