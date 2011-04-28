/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import java.util.List;

/**
 * Interface for web service providing user's data remotely.
 *
 * @author Petr Miko
 */
@WebService
public interface UserDataService {

    /**
     * Method just for checking web service availability
     * (user needs to connect through Spring security but doesn't wish to do anything more)
     *
     * @return true
     */
    public boolean isServiceAvailable();

    /**
     * Method returning List of information about available experiments.
     *
     * @param rights defines rights that user has in desired experiments (user, subject)
     * @return List of information about available experiments
     */
    public List<ExperimentInfo> getExperiments (Rights rights);

    /**
     * Method returning list of files, which belong to experiment defined by id.
     *
     * @param experimentId Number defining explored experiment
     * @return List of information about experiment's data files
     * @throws SOAPException wrapped SQLException
     */
    public List<DataFileInfo> getExperimentFiles(int experimentId)
            throws SOAPException;

    /**
     * Method streaming desired file back to user.
     *
     * @param dataFileId Id of file to download
     * @return Stream of bytes (file)
     * @throws SOAPException Wrapped SQLException and IOException
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler downloadFile(int dataFileId) throws SOAPException;
}
