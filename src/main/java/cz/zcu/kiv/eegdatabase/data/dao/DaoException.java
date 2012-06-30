package cz.zcu.kiv.eegdatabase.data.dao;

/**
 * Exception class for DAO purposes.
 *
 * @author Petr Miko
 */
public class DaoException extends Exception {

    /**
     * Wrapping original error with custom message.
     *
     * @param message commentary message
     * @param error   original exception
     */
    public DaoException(String message, Throwable error) {
        super(message, error);
    }

    /**
     * New exception with custom message.
     *
     * @param message custom error message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Wrapping original error.
     *
     * @param error original exception
     */
    public DaoException(Throwable error) {
        super(error);
    }
}
