package cz.zcu.kiv.eegdatabase.webservices.rest.common.exception;

/**
 * Exception thrown when required resource is not on provided location.
 *
 * @author Petr Miko
 */
public class RestNotFoundException extends Exception {

    public RestNotFoundException() {
    }

    public RestNotFoundException(Throwable error) {
        super(error);
    }

    public RestNotFoundException(String message) {
        super(message);
    }
}
