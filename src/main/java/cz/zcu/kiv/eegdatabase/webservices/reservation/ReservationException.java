package cz.zcu.kiv.eegdatabase.webservices.reservation;

/**
 * Exception for notifying something went wrong during reservation process.
 *
 * @author Petr Miko
 */
public class ReservationException extends Exception {

    public ReservationException(){}

    public ReservationException(Throwable error) {
        super(error);
    }

    public ReservationException(String message) {
        super(message);
    }
}
