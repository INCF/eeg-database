package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;

import java.util.Locale;

/**
 * Interface of service, which provides basic operations upon user part of eeg database, eg. login or creating new users.
 *
 * @author Petr Miko
 */
public interface UserService {

    /**
     * Creates new user inside eeg base.
     *
     * @param registrationPath URL of registration confirmation page
     * @param cmd new person/user information
     * @param locale used locale
     * @return basic information about created person/user
     * @throws RestServiceException error during person/user creation process
     */
    public UserInfo create(String registrationPath, AddPersonCommand cmd, Locale locale) throws RestServiceException;

    /**
     * Method for verifying user's credentials.
     * @return basic information about accessing person/user
     */
    public UserInfo login();
}
