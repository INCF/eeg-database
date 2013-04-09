package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.wrappers.RecordCountData;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class mapping REST experiment service.
 *
 * @author Petr Miko
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/experiments")
public class ExperimentServiceController {

    @Autowired
    private ExperimentService service;

    /**
     * Creates new experiment record.
     *
     * @param experiment experiment to be created
     * @return primary key
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ExperimentData createExperiment(@RequestBody ExperimentData experiment) {
        Integer pk = service.createExperiment(experiment);

        if (pk != null) {
            experiment.setExperimentId(pk);
            return experiment;
        }
        return null;
    }

    /**
     * Getter of public experiments.
     *
     * @param fromId from which id should service fetch records
     * @param count  maximum records fetched
     * @return public experiments
     */
    @RequestMapping(value = "/public/{fromId}/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int fromId, @PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(fromId, count));
    }

    /**
     * Getter of public experiments count.
     *
     * @return public experiments count
     */
    @RequestMapping(value = "/public/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(0, count));
    }

    /**
     * Getter of count of public and user's experiments
     *
     * @return experiments count
     */
    @RequestMapping(value = "/count")
    public RecordCountData getCount() {
        RecordCountData countData = new RecordCountData();
        countData.setMyRecords(service.getMyExperiments().size());
        countData.setPublicRecords(service.getPublicExperimentsCount());
        return countData;
    }

    /**
     * Getter of user's experiments.
     *
     * @return user's experiments
     */
    @RequestMapping(value = "/mine")
    public ExperimentDataList getMyExperiments() {
        return new ExperimentDataList(service.getMyExperiments());
    }

    /**
     * Getter of created artifacts.
     *
     * @return artifacts
     */
    @RequestMapping(value = "/artifacts")
    public ArtifactDataList getArtifacts() {
        return new ArtifactDataList(service.getArtifacts());
    }

    /**
     * Creates new artifact record.
     *
     * @param artifact artifact to be created
     * @return primary key
     */
    @RequestMapping(value = "/artifacts", method = RequestMethod.POST)
    public ArtifactData createArtifact(@RequestBody ArtifactData artifact) {
        Integer pk = service.createArtifact(artifact);

        if (pk != null) {
            artifact.setArtifactId(pk);
            return artifact;
        } else
            return null;
    }

    /**
     * Getter of available digitizations.
     *
     * @return digitizations
     */
    @RequestMapping(value = "/digitizations")
    public DigitizationDataList getDigitizations() {
        return new DigitizationDataList(service.getDigitizations());
    }

    /**
     * Creates new digitization record.
     *
     * @param digitization digitization to be created
     * @return primary key
     */
    @RequestMapping(value = "/digitizations", method = RequestMethod.POST)
    public DigitizationData createDigitization(@RequestBody DigitizationData digitization) {
        Integer pk = service.createDigitization(digitization);

        if (pk != null) {
            digitization.setDigitizationId(pk);
            return digitization;
        } else
            return null;
    }

    /**
     * Getter of registered diseases.
     *
     * @return diseases
     */
    @RequestMapping("/diseases")
    public DiseaseDataList getDiseases() {
        return new DiseaseDataList(service.getDiseases());
    }

    /**
     * Creates new disease record.
     *
     * @param disease disease to be created
     * @return primary key
     */
    @RequestMapping(value = "/diseases", method = RequestMethod.POST)
    public DiseaseData createDisease(@RequestBody DiseaseData disease) {
        Integer pk = service.createDisease(disease);

        if (pk != null) {
            disease.setDiseaseId(pk);
            return disease;
        } else
            return null;
    }

    /**
     * Getter of registered hardware types.
     *
     * @return registered hardware types
     */
    @RequestMapping("/hardwareList")
    public HardwareDataList getHardwareList() {
        return new HardwareDataList(service.getHardwareList());
    }

    /**
     * Getter of registered software types.
     *
     * @return registered software types
     */
    @RequestMapping("/softwareList")
    public SoftwareDataList getSoftwareList() {
        return new SoftwareDataList(service.getSoftwareList());
    }

    /**
     * Getter of registered weather types.
     *
     * @return weather types
     */
    @RequestMapping("/weatherList/{groupId}")
    public WeatherDataList getWeatherList(@PathVariable int groupId) {
        return new WeatherDataList(service.getWeatherList(groupId));
    }

    /**
     * Creates new weather record for specified research group.
     *
     * @param weather     weather record to be created
     * @param groupId research group for which is record being created
     * @return record with identifier
     */
    @RequestMapping(value ="/weatherList/{groupId}", method = RequestMethod.POST)
    public WeatherData createWeather(@RequestBody WeatherData weather, @PathVariable int groupId) {
        Integer pk = service.createWeather(weather, groupId);

        if (pk != null) {
            weather.setWeatherId(pk);
            return weather;
        } else return null;

    }

    /**
     * Getter of registered pharmaceuticals.
     *
     * @return pharmaceuticals
     */
    @RequestMapping("/pharmaceuticals")
    public PharmaceuticalDataList getPharmaceuticals() {
        return new PharmaceuticalDataList(service.getPharmaceuticals());
    }

    /**
     * Getter of existing electrode locations.
     *
     * @return electrode locations
     */
    @RequestMapping("/electrodeLocations")
    public ElectrodeLocationDataList getElectrodeLocations() {
        return new ElectrodeLocationDataList(service.getElectrodeLocations());
    }

    /**
     * Method for creating new record of electrode location.
     *
     * @param electrodeLocation record to be created
     * @return identifier of newly created record
     */
    @RequestMapping(value = "/electrodeLocations", method = RequestMethod.POST)
    public ElectrodeLocationData createElectrodeLocation(@RequestBody ElectrodeLocationData electrodeLocation) {
        Integer pk = service.createElectrodeLocation(electrodeLocation);

        if (pk != null) {
            electrodeLocation.setId(pk);
            return electrodeLocation;
        } else return null;
    }

    /**
     * Getter of existing electrode fixes.
     *
     * @return electrode fixes
     */
    @RequestMapping("/electrodeFixList")
    public ElectrodeFixDataList getElectrodeFixList() {
        return new ElectrodeFixDataList(service.getElectrodeFixList());
    }

    /**
     * Creates new electrode fix record.
     *
     * @param fix electrode fix to be created
     * @return primary key
     */
    @RequestMapping(value = "/electrodeFixList", method = RequestMethod.POST)
    public ElectrodeFixData createElectrodeFix(@RequestBody ElectrodeFixData fix) {
        Integer pk = service.createElectrodeFix(fix);

        if (pk != null) {
            fix.setId(pk);
            return fix;
        } else return null;
    }

    /**
     * Getter of existing electrode types.
     *
     * @return electrode types
     */
    @RequestMapping("/electrodeTypes")
    public ElectrodeTypeDataList getElectrodeTypes() {
        return new ElectrodeTypeDataList(service.getElectrodeTypes());
    }

    /**
     * Getter of available electrode systems.
     *
     * @return electrode systems
     */
    @RequestMapping("/electrodeSystems")
    public ElectrodeSystemDataList getElectrodeSystems() {
        return new ElectrodeSystemDataList(service.getElectrodeSystems());
    }

}
