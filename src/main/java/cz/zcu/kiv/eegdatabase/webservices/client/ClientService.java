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
 *   ClientService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.client;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;

import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.DataFileInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.EducationLevelInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentOptParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentOptParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.FileMetadataParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.FileMetadataParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.HardwareInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonOptParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonOptParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupMembershipInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ScenarioInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.WeatherInfo;

/**
 * Web service that is used by EEGCLIENT application. It provides methods for
 * adding new data to the database and methods for sending existing data.
 * 
 * @author František Liška
 */
@MTOM
@WebService
public interface ClientService {

	/**
	 * Specifies whether service is available for the user.
	 * 
	 * @return True if it is available.
	 */
	boolean isServiceAvailable();

	/**
	 * Returns a list filled with information about existing people.
	 * 
	 * @return List with info.
	 */
	public List<PersonInfo> getPersonList();

	/**
	 * Returns a list filled with information about existing education levels.
	 * 
	 * @return List with info.
	 */
	public List<EducationLevelInfo> getEducationLevelList();

	/**
	 * Returns a list filled with information about existing research groups.
	 * 
	 * @return List with info.
	 */
	public List<ResearchGroupInfo> getResearchGroupList();

	/**
	 * Returns a list filled with information about existing people.
	 * 
	 * @return List with info.
	 */
	public List<ScenarioInfo> getScenarioList();

	/**
	 * Returns a list filled with information about existing values for optional
	 * parameters for people.
	 * 
	 * @return List with info.
	 */
	public List<PersonOptParamValInfo> getPersonOptParamValList();

	/**
	 * Returns a default group with information about existing default lists
	 * (weather, hardware, optional parameters etc.).
	 * 
	 * @return Default research group.
	 */
	public ResearchGroupInfo getDefaultLists();

	/**
	 * Adds a new hardware definition based on information provided by info
	 * object.
	 * 
	 * @param info Information about hardware.
	 * @return Primary key of the newly created record.
	 */
	public int addHardware(HardwareInfo info);

	/**
	 * Adds a new weather definition based on information provided by info
	 * object.
	 * 
	 * @param info Information about weather.
	 * @return Primary key of the newly created record.
	 */
	public int addWeather(WeatherInfo info);

	/**
	 * Adds a new research group based on information provided by info object.
	 * 
	 * @param info Information about research group.
	 * @return Primary key of the newly created record.
	 */
	public int addResearchGroup(ResearchGroupInfo info);

	/**
	 * Adds a new research group membership based on information provided by
	 * info object.
	 * 
	 * @param info Information about research group membership.
	 */
	public void addResearchGroupMembership(ResearchGroupMembershipInfo info);

	/**
	 * Adds a new person based on information provided by info object.
	 * 
	 * @param info Information about person.
	 * @return Primary key of the newly created record.
	 */
	public int addPerson(PersonInfo info);

	/**
	 * Adds a new scenario based on the information provided by info object.
	 * Scenario can have a file attached to it.
	 * 
	 * @param info Information about scenario.
	 * @param inputData Data handler with an input stream which contains a file.
	 *            Can be null.
	 * @return Primary key of the newly created record.
	 * @throws ClientServiceException If I/O operation occurs.
	 */
	public int addScenario(ScenarioInfo info, @XmlMimeType("application/octet-stream") DataHandler inputData) throws ClientServiceException;

	/**
	 * Adds a new data file based on the information provided by info object.
	 * Data file has a file attached to it.
	 * 
	 * @param info Information about the data file.
	 * @param inputData Data handler with an input stream which contains a file.
	 * @return Primary key of the newly created record.
	 * @throws ClientServiceException If I/O operation occurs.
	 */
	public int addDataFile(DataFileInfo info, @XmlMimeType("application/octet-stream") DataHandler inputData) throws ClientServiceException;

	/**
	 * Adds a new optional parameter for people definition based on the
	 * information provided by info object.
	 * 
	 * @param info Information about the definition.
	 * @return Primary key of the newly created record.
	 */
	public int addPersonOptParamDef(PersonOptParamDefInfo info);

	/**
	 * Adds a new optional parameter for person value based on information
	 * provided by info object.
	 * 
	 * @param info Information about the value.
	 */
	public void addPersonOptParamVal(PersonOptParamValInfo info);

	/**
	 * Adds a new experiment based on information provided by info object.
	 * 
	 * @param info Information about experiment.
	 * @return Primary key of the newly created record.
	 */
	public int addExperiment(ExperimentInfo info);

	/**
	 * Adds a new optional parameter for experiment definition based on the
	 * information provided by info object.
	 * 
	 * @param info Information about the definition.
	 * @return Primary key of the newly created record.
	 */
	public int addExperimentOptParamDef(ExperimentOptParamDefInfo info);

	/**
	 * Adds a new optional parameter for experiment value based on information
	 * provided by info object.
	 * 
	 * @param info Information about the value.
	 */
	public void addExperimentOptParamVal(ExperimentOptParamValInfo info);

	/**
	 * Adds a new metadata definition based on the information provided by info
	 * object.
	 * 
	 * @param info Information about the definition.
	 * @return Primary key of the newly created record.
	 */
	public int addFileMetadataParamDef(FileMetadataParamDefInfo info);

	/**
	 * Adds a new metadata value based on information provided by info object.
	 * 
	 * @param info Information about the value.
	 */
	public void addFileMetadataParamVal(FileMetadataParamValInfo info);
}
