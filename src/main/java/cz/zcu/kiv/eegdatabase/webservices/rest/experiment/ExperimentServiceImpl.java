package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Service
@Transactional
public class ExperimentServiceImpl implements ExperimentService {

    private final Comparator<ExperimentData> idComparator = new Comparator<ExperimentData>() {
        @Override
        public int compare(ExperimentData id1, ExperimentData id2) {
            return id1.getExperimentId() - id2.getExperimentId();
        }
    };
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentData> getPublicExperiments(int from, int max) {
        return fillAndSort(experimentDao.getVisibleExperiments(personDao.getLoggedPerson().getPersonId(), from, max));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentData> getMyExperiments() {
        Person loggedUser = personDao.getLoggedPerson();
        return fillAndSort(loggedUser.getExperimentsForOwnerId());
    }

    @Override
    public int getPublicExperimentsCount() {
        return experimentDao.getVisibleExperimentsCount(personDao.getLoggedPerson().getPersonId());
    }

    private List<ExperimentData> fillAndSort(Collection<Experiment> exps) {
        List<ExperimentData> experiments = new ArrayList<ExperimentData>(exps.size());

        for (Experiment exp : exps) {
            ExperimentData expData = new ExperimentData();
            expData.setExperimentId(exp.getExperimentId());
            expData.setStartTime(exp.getStartTime());
            expData.setEndTime(exp.getEndTime());

            ScenarioSimpleData scenarioData = new ScenarioSimpleData();
            scenarioData.setScenarioId(exp.getScenario().getScenarioId());
            scenarioData.setScenarioName(exp.getScenario().getTitle());

            WeatherData weatherData = new WeatherData();
            Weather weather = exp.getWeather();
            weatherData.setWeatherId(weather.getWeatherId());
            weatherData.setTitle(weather.getTitle());
            weatherData.setDescription(weather.getDescription());

            Person subject = exp.getPersonBySubjectPersonId();
            SubjectData subjectData = new SubjectData();
            subjectData.setPersonId(subject.getPersonId());
            subjectData.setName(subject.getGivenname());
            subjectData.setSurname(subject.getSurname());
            subjectData.setLeftHanded(subject.getLaterality() == 'L' || subject.getLaterality() == 'l');
            subjectData.setGender(subject.getGender());
            subjectData.setAge(Years.yearsBetween(new LocalDate(subject.getDateOfBirth()), new LocalDate()).getYears());
            subjectData.setMail(subject.getUsername());

            Artifact artifact = exp.getArtifact();
            ArtifactData artifactData = new ArtifactData();
            artifactData.setArtifactId(artifact.getArtifactId());
            artifactData.setCompensation(artifact.getCompensation());
            artifactData.setRejectCondition(artifact.getRejectCondition());

            Set<Disease> diseases = exp.getDiseases();
            List<DiseaseData> diseaseDatas = new ArrayList<DiseaseData>();
            for (Disease dis : diseases) {
                DiseaseData diseaseData = new DiseaseData();
                diseaseData.setDiseaseId(dis.getDiseaseId());
                diseaseData.setName(dis.getTitle());
                diseaseData.setDescription(dis.getDescription());
            }

            Digitization digitization = exp.getDigitization();
            DigitizationData dgData = new DigitizationData();
            dgData.setDigitizationId(digitization.getDigitizationId());
            dgData.setGain(digitization.getGain());
            dgData.setFilter(digitization.getFilter());
            dgData.setSamplingRate(digitization.getSamplingRate());

            Set<Hardware> hardwares = exp.getHardwares();
            List<HardwareData> hardwareDatas = new ArrayList<HardwareData>();
            for (Hardware h : hardwares) {
                HardwareData hw = new HardwareData();
                hw.setHardwareId(h.getHardwareId());
                hw.setTitle(h.getTitle());
                hw.setType(h.getType());
                hw.setDefaultNumber(h.getDefaultNumber());
                hardwareDatas.add(hw);
            }

            ElectrodeConf elConf = exp.getElectrodeConf();
            ElectrodeConfData electrodeConfData = new ElectrodeConfData();
            //set primitive types
            electrodeConfData.setId(elConf.getElectrodeConfId());
            electrodeConfData.setId(elConf.getImpedance());
            //set electrode system
            ElectrodeSystem elSystem = elConf.getElectrodeSystem();
            ElectrodeSystemData electrodeSystemData = new ElectrodeSystemData();
            electrodeSystemData.setId(elSystem.getElectrodeSystemId());
            electrodeSystemData.setDescription(elSystem.getDescription());
            electrodeSystemData.setTitle(elSystem.getTitle());
            electrodeSystemData.setDefaultNumber(elSystem.getDefaultNumber());
            electrodeConfData.setElectrodeSystem(electrodeSystemData);
            //set electrode locations
            Set<ElectrodeLocation> elLocations = elConf.getElectrodeLocations();

            if (elLocations != null && !elLocations.isEmpty()) {
                List<ElectrodeLocationData> electrodeLocations = new ArrayList<ElectrodeLocationData>(elLocations.size());

                for (ElectrodeLocation el : elLocations) {
                    ElectrodeLocationData electrodeLocation = new ElectrodeLocationData();
                    //set primitives
                    electrodeLocation.setId(el.getElectrodeLocationId());
                    electrodeLocation.setTitle(el.getTitle());
                    electrodeLocation.setDescription(el.getDescription());
                    electrodeLocation.setAbbr(el.getShortcut());
                    electrodeLocation.setDefaultNumber(el.getDefaultNumber());
                    //set complex types
                    ElectrodeFix elFix = el.getElectrodeFix();
                    ElectrodeFixData electrodeFix = new ElectrodeFixData();
                    electrodeFix.setId(elFix.getElectrodeFixId());
                    electrodeFix.setTitle(elFix.getTitle());
                    electrodeFix.setDescription(elFix.getDescription());
                    electrodeFix.setDefaultNumber(elFix.getDefaultNumber());
                    electrodeLocation.setElectrodeFix(electrodeFix);

                    ElectrodeType elType = el.getElectrodeType();
                    ElectrodeTypeData electrodeType = new ElectrodeTypeData();
                    electrodeType.setId(elType.getElectrodeTypeId());
                    electrodeType.setTitle(elType.getTitle());
                    electrodeType.setDescription(elType.getDescription());
                    electrodeType.setDefaultNumber(elType.getDefaultNumber());
                    electrodeLocation.setElectrodeType(electrodeType);

                    electrodeLocations.add(electrodeLocation);
                }
                electrodeConfData.setElectrodeLocations(new ElectrodeLocationDataList(electrodeLocations));
            }

            Set<Pharmaceutical> pharms = exp.getPharmaceuticals();
            List<PharmaceuticalData> pharmaceuticals = new ArrayList<PharmaceuticalData>();
            if (pharms != null && !pharms.isEmpty()) {
                for (Pharmaceutical p : pharms) {
                    PharmaceuticalData pharmaceutical = new PharmaceuticalData();
                    pharmaceutical.setId(p.getPharmaceuticalId());
                    pharmaceutical.setTitle(p.getTitle());
                    pharmaceutical.setDescription(p.getDescription());
                    pharmaceuticals.add(pharmaceutical);
                }
            }

            Set<Software> softs = exp.getSoftwares();
            List<SoftwareData> softwareList = new ArrayList<SoftwareData>();
            if (softs != null && !softs.isEmpty()) {
                for (Software s : softs) {
                    SoftwareData software = new SoftwareData();
                    software.setId(s.getSoftwareId());
                    software.setTitle(s.getTitle());
                    software.setDescription(s.getDescription());
                    software.setDefaultNumber(s.getDefaultNumber());
                    softwareList.add(software);
                }
            }

            ResearchGroup rg = exp.getResearchGroup();
            ResearchGroupData researchGroup = new ResearchGroupData();

            researchGroup.setGroupId(rg.getResearchGroupId());
            researchGroup.setGroupName(rg.getTitle());

            Person ow = exp.getPersonByOwnerId();
            OwnerData owner = new OwnerData();
            String mail[] = ow.getUsername().split("@");
            owner.setId(ow.getPersonId());
            owner.setName(ow.getGivenname());
            owner.setSurname(ow.getSurname());
            if (mail.length == 2) {
                owner.setMailUsername(mail[0]);
                owner.setMailDomain(mail[1]);
            }

            expData.setOwner(owner);
            expData.setResearchGroup(researchGroup);
            expData.setPharmaceuticals(new PharmaceuticalDataList(pharmaceuticals));
            expData.setSoftwareList(new SoftwareDataList(softwareList));
            expData.setElectrodeConf(electrodeConfData);
            expData.setHardwareList(new HardwareDataList(hardwareDatas));
            expData.setScenario(scenarioData);
            expData.setArtifact(artifactData);
            expData.setSubject(subjectData);
            expData.setDiseases(new DiseaseDataList(diseaseDatas));
            expData.setDigitization(dgData);
            expData.setWeather(weatherData);
            experiments.add(expData);
        }
        Collections.sort(experiments, idComparator);
        return experiments;
    }
}
