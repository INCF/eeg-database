package cz.zcu.kiv.eegdatabase.webservices.client;

/**
 * Exception which is thrown when there is an error during the communication
 * with the client.
 * 
 * @author František Liška
 */
public class ClientServiceException extends Exception {

	/**
	 * Constructor, adds a an error message to the exception.
	 * 
	 * @param throwable Original throwable.
	 */
	public ClientServiceException(Throwable throwable) {
		super("Error occured during working with client web service.", throwable);
	}
}
