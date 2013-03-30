package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;

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
     * @return weather types
     */
    public List<WeatherData> getWeatherList();

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
     * @param artifact artifact to be created
     * @return primary key
     */
    public Integer createArtifact(ArtifactData artifact);
}
