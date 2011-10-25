/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.SyncChanges;
import cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers.*;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
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
     * Method returning List of available weather information.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return weather information
     */
    public List<WeatherInfo> getWeather(long oracleScn);

    /**
     * Method returning List of information about available experiments.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return List of information about available experiments
     */
    public List<ExperimentInfo> getExperiments(long oracleScn);

    /**
     * Method returning List of information about available scenarios.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return list of scenarios
     */
    public List<ScenarioInfo> getScenarios(long oracleScn);

    /**
     * Method for obtaining list of all EEG base users.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return list of users
     */
    public List<PersonInfo> getPeople(long oracleScn);

    /**
     * Method for obtaining list of all EEG base research groups.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return list of research groups
     */
    public List<ResearchGroupInfo> getResearchGroups(long oracleScn);

    /**
     * Method returning List of information about available data files.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return list of data files
     */
    public List<DataFileInfo> getDataFiles(long oracleScn) throws DataDownloadException;

    /**
     * Method returning List of information about available hardware.
     *
     * @param oracleScn number of last revision (Oracle SCN value)
     * @return list of hardware
     */
    public List<HardwareInfo> getHardware(long oracleScn);

    /**
     * Method streaming desired file back to user.
     *
     * @param dataFileId Id of file to download
     * @return Stream of bytes (file)
     * @throws DataDownloadException exception occurred on side of web service
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler downloadDataFile(int dataFileId) throws DataDownloadException;
}
