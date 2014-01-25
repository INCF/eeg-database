/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   UserDataService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.webservices.datadownload;


import cz.zcu.kiv.eegdatabase.webservices.datadownload.wrappers.*;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import java.io.InputStream;
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
     * @return weather information
     */
    public List<WeatherInfo> getWeather();

    /**
     * Method returning List of information about available experiments.
     *
     * @return List of information about available experiments
     */
    public List<ExperimentInfo> getExperiments();

    /**
     * Method returning List of information about available scenarios.
     *
     * @return list of scenarios
     */
    public List<ScenarioInfo> getScenarios();

    /**
     * Method for obtaining list of all EEG base users.
     *
     * @return list of users
     */
    public List<PersonInfo> getPeople();

    /**
     * Method for obtaining list of all EEG base research groups.
     *
     * @return list of research groups
     */
    public List<ResearchGroupInfo> getResearchGroups();

    /**
     * Method returning List of information about available data files.
     *
     * @return list of data files
     */
    public List<DataFileInfo> getDataFiles() throws UserDataServiceException;

    /**
     * Method returning List of information about available hardware.
     *
     * @return list of hardware
     */
    public List<HardwareInfo> getHardware();

    /**
     * Method streaming desired file back to user.
     *
     * @param dataFileId Id of file to download
     * @return Stream of bytes (file)
     * @throws UserDataServiceException exception occurred on side of web service
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler downloadDataFile(int dataFileId) throws UserDataServiceException;

    /**
     * Method to add or update Data File record.
     * @param dataFile data file information
     * @param inputData data file content
     * @return identifier (primary key)
     */
    public int addOrUpdateDataFile(DataFileInfo dataFile, @XmlMimeType("application/octet-stream") DataHandler inputData) throws UserDataServiceException;

    /**
     * Method to add or update Experiment record.
     * @param experiment experiment information
     * @return identifier (primary key)
     */
    public int addOrUpdateExperiment(ExperimentInfo experiment);

    /**
     * Method to add or update Hardware record.
     * @param hardware hardware type information
     * @return identifier (primary key)
     */
    public int addOrUpdateHardware(HardwareInfo hardware);

    /**
     * Method to add or update Person record.
     * @param person person information
     * @return identifier (primary key)
     */
    public int addOrUpdatePerson(PersonInfo person);

    /**
     * Method to add or update Research Group record.
     * @param group research group type information
     * @return identifier (primary key)
     */
    public int addOrUpdateResearchGroup(ResearchGroupInfo group);

    /**
     * Method to add or update Scenario record.
     * @param scenario scenario type information
     * @return identifier (primary key)
     */
    public int addOrUpdateScenario(ScenarioInfo scenario);


    /**
     * Method to add or update Weather record.
     * @param weather weather type information
     * @return identifier (primary key)
     */
    public int addOfUpdateWeather(WeatherInfo weather);
}
