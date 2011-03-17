/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;
import java.util.List;

/**
 *
 * @author Petr Miko
 */
@MTOM
@WebService
public interface UserDataService {

    public boolean isServiceAvailable();

    public List<ExperimentInfo> getAvailableExperimentsWithRights(Rights rights);

    public List<DataFileInfo> getExperimentDataFilesWhereExpId(int experimentId) throws WebServiceException;

    public DataHandler getDataFileBinaryWhereFileId(int dataFileId) throws WebServiceException;
}
