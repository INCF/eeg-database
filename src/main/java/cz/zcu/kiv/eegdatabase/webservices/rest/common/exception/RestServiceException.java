package cz.zcu.kiv.eegdatabase.webservices.rest.common.exception;

/**
 * Exception for notifying something went wrong during reservation process.
 *
 * @author Petr Miko
 */
public class RestServiceException extends Exception {

    public RestServiceException(){}

    public RestServiceException(Throwable error) {
        super(error);
    }

    public RestServiceException(String message) {
        super(message);
    }
}
