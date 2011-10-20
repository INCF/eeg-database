package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * User: Petr Miko - miko.petr (at) gmail.com
 * Date: 3.10.11
 * Time: 10:20
 * <p/>
 * This class is for exception purposes within web services.
 */
public class DataDownloadException extends Exception {

    /**
     * Constructor, adds custom message.
     *
     * @param throwable original throwable
     */
    public DataDownloadException(Throwable throwable) {
        super("Error occured during working with web service.", throwable);
    }
}
