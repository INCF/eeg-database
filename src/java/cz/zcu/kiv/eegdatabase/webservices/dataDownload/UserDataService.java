/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import javax.jws.WebService;
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

    public List<ExperimentInfo> getAvailableExperiments(Rights rights);

    public List<DataFileInfo> getExperimentDataFiles(int experimentID);
}
