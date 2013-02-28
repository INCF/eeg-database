package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;

import java.util.Locale;

/**
 * @author Petr Miko
 *         Date: 10.2.13
 */
public interface UserService {

    public UserInfo create(String registrationPath, AddPersonCommand cmd, Locale locale) throws RestServiceException;

    public UserInfo login();
}
