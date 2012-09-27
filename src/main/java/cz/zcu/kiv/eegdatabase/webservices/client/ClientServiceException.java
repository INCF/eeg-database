package cz.zcu.kiv.eegdatabase.webservices.client;

/**
 * @author František Liška
 */
public class ClientServiceException extends Exception {

    /**
     * Constructor, adds custom message.
     *
     * @param throwable original throwable
     */
    public ClientServiceException(Throwable throwable) {
        super("Error occured during working with client web service.", throwable);
    }
}
