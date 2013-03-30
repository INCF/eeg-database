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
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("/experiments")
public class ExperimentServiceController {

    @Autowired
    private ExperimentService service;

    @RequestMapping(value = "/public/{fromId}/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int fromId, @PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(fromId, count));
    }

    @RequestMapping(value = "/public/{count}")
    public ExperimentDataList getPublicExperiments(@PathVariable int count) {
        return new ExperimentDataList(service.getPublicExperiments(0, count));
    }

    @RequestMapping(value = "/count")
    public RecordCountData getCount() {
        RecordCountData countData = new RecordCountData();
        countData.setMyRecords(service.getMyExperiments().size());
        countData.setPublicRecords(service.getPublicExperimentsCount());
        return countData;
    }

    @RequestMapping(value = "/mine")
    public ExperimentDataList getMyExperiments() {
        return new ExperimentDataList(service.getMyExperiments());
    }

    @RequestMapping(value = "/artifacts")
    public ArtifactDataList getArtifacts() {
        return new ArtifactDataList(service.getArtifacts());
    }

    @RequestMapping(value = "/artifacts", method = RequestMethod.POST)
    public ArtifactData createArtifact(@RequestBody ArtifactData artifact) {
        Integer pk = service.createArtifact(artifact);

        if (pk != null) {
            artifact.setArtifactId(pk);
            return artifact;
        } else
            return null;
    }

    @RequestMapping(value = "/digitizations")
    public DigitizationDataList getDigitizations() {
        return new DigitizationDataList(service.getDigitizations());
    }

    @RequestMapping(value = "/digitizations", method = RequestMethod.POST)
    public DigitizationData createDigitization(@RequestBody DigitizationData digitization) {
        Integer pk = service.createDigitization(digitization);

        if (pk != null) {
            digitization.setDigitizationId(pk);
            return digitization;
        } else
            return null;
    }

    @RequestMapping("/diseases")
    public DiseaseDataList getDiseases() {
        return new DiseaseDataList(service.getDiseases());
    }

    @RequestMapping(value = "/diseases", method = RequestMethod.POST)
    public DiseaseData createDisease(@RequestBody DiseaseData disease) {
        Integer pk = service.createDisease(disease);

        if (pk != null) {
            disease.setDiseaseId(pk);
            return disease;
        } else
            return null;
    }

    @RequestMapping("/hardwareList")
    public HardwareDataList getHardwareList() {
        return new HardwareDataList(service.getHardwareList());
    }

    @RequestMapping("/softwareList")
    public SoftwareDataList getSoftwareList() {
        return new SoftwareDataList(service.getSoftwareList());
    }

    @RequestMapping("/weatherList")
    public WeatherDataList getWeatherList() {
        return new WeatherDataList(service.getWeatherList());
    }

    @RequestMapping("/pharmaceuticals")
    public PharmaceuticalDataList getPharmaceuticals() {
        return new PharmaceuticalDataList(service.getPharmaceuticals());
    }

    @RequestMapping("/electrodeLocations")
    public ElectrodeLocationDataList getElectrodeLocations() {
        return new ElectrodeLocationDataList(service.getElectrodeLocations());
    }

    @RequestMapping(value = "/electrodeLocations", method = RequestMethod.POST)
    public ElectrodeLocationData createElectrodeLocation(@RequestBody ElectrodeLocationData electrodeLocation) {
        Integer pk = service.createElectrodeLocation(electrodeLocation);

        if (pk != null) {
            electrodeLocation.setId(pk);
            return electrodeLocation;
        } else return null;
    }

    @RequestMapping("/electrodeFixList")
    public ElectrodeFixDataList getElectrodeFixList() {
        return new ElectrodeFixDataList(service.getElectrodeFixList());
    }

    @RequestMapping("/electrodeTypes")
    public ElectrodeTypeDataList getElectrodeTypes() {
        return new ElectrodeTypeDataList(service.getElectrodeTypes());
    }

    @RequestMapping("/electrodeSystems")
    public ElectrodeSystemDataList getElectrodeSystems() {
        return new ElectrodeSystemDataList(service.getElectrodeSystems());
    }

}
