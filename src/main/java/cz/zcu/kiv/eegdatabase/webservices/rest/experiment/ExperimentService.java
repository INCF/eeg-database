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
 *   ExperimentService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;
import org.apache.wicket.ajax.json.JSONObject;

import java.util.List;

/**
 * Service interface for handling experiment data on REST service.
 *
 * @author Petr Miko
 */
public interface ExperimentService {

    /**
     * Getter of public experiments.
     *
     * @param fromId from which id should service fetch records
     * @param count  maximum records fetched
     * @return public experiments
     */
    public List<ExperimentData> getPublicExperiments(int fromId, int count);

    /**
     * Getter of user's experiments.
     *
     * @return user's experiments
     */
    public List<ExperimentData> getMyExperiments();

    /**
     * Getter of public experiments count.
     *
     * @return public experiments count
     */
    public int getPublicExperimentsCount();

    /**
     * Getter of created artifacts.
     *
     * @return artifacts
     */
    public List<ArtifactData> getArtifacts();

    /**
     * Getter of available digitizations.
     *
     * @return digitizations
     */
    public List<DigitizationData> getDigitizations();

    /**
     * Getter of registered diseases.
     *
     * @return diseases
     */
    public List<DiseaseData> getDiseases();

    /**
     * Getter of registered weather types.
     *
     * @param groupId identifier of a user's research group
     * @return weather types
     */
    public List<WeatherData> getWeatherList(int groupId);

    /**
     * Getter of registered hardware types.
     *
     * @return registered hardware types
     */
    public List<HardwareData> getHardwareList();

    /**
     * Getter of registered software types.
     *
     * @return registered software types
     */
    public List<SoftwareData> getSoftwareList();

    /**
     * Getter of registered pharmaceuticals.
     *
     * @return pharmaceuticals
     */
    public List<PharmaceuticalData> getPharmaceuticals();

    /**
     * Getter of available electrode systems.
     *
     * @return electrode systems
     */
    public List<ElectrodeSystemData> getElectrodeSystems();

    /**
     * Getter of existing electrode types.
     *
     * @return electrode types
     */
    public List<ElectrodeTypeData> getElectrodeTypes();

    /**
     * Getter of existing electrode fixes.
     *
     * @return electrode fixes
     */
    public List<ElectrodeFixData> getElectrodeFixList();

    /**
     * Getter of existing electrode locations.
     *
     * @return electrode locations
     */
    public List<ElectrodeLocationData> getElectrodeLocations();

    /**
     * Method for creating new record of electrode location.
     *
     * @param electrodeLocation record to be created
     * @return identifier of newly created record
     */
    public Integer createElectrodeLocation(ElectrodeLocationData electrodeLocation);

    /**
     * Creates new digitization record.
     *
     * @param digitization digitization to be created
     * @return primary key
     */
    public Integer createDigitization(DigitizationData digitization);

    /**
     * Creates new disease record.
     *
     * @param disease disease to be created
     * @return primary key
     */
    public Integer createDisease(DiseaseData disease);

    /**
     * Creates new artifact record.
     *
     * @param artifact artifact to be created
     * @return primary key
     */
    public Integer createArtifact(ArtifactData artifact);

    /**
     * Creates new electrode fix record.
     *
     * @param fix electrode fix to be created
     * @return primary key
     */
    public Integer createElectrodeFix(ElectrodeFixData fix);

    /**
     * Creates new experiment record.
     *
     * @param experiment experiment to be created
     * @return primary key
     */
    public Integer createExperiment(ExperimentData experiment);

    /**
     * Creates new weather record.
     *
     * @param weatherData     weather record to be created.
     * @param researchGroupId identifier of research group for which is weather record created
     * @return primary key
     */
    public Integer createWeather(WeatherData weatherData, int researchGroupId);

    /**
     * Adds odML metadata to experiment
     *
     * @param id     experiment ID
     * @param data odML data to be inserted to experiment
     * @return experiment
     */
    public Experiment addMobioMetadata(int id, JSONObject data);
}
