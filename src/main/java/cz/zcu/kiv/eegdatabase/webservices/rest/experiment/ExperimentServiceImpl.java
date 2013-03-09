package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import org.eclipse.core.internal.dtree.DataTreeLookup;
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
        return fillAndSort( experimentDao.getVisibleExperiments(personDao.getLoggedPerson().getPersonId(), from, max));
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


    private List<ExperimentData> fillAndSort(Collection<Experiment> exps){
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

            Artifact artifact = exp.getArtifact();
            ArtifactData artifactData = new ArtifactData();
            artifactData.setArtifactId(artifact.getArtifactId());
            artifactData.setCompensation(artifact.getCompensation());
            artifactData.setRejectCondition(artifact.getRejectCondition());

            Set<Disease> diseases = exp.getDiseases();
            List<DiseaseData> diseaseDatas = new ArrayList<DiseaseData>();
            for(Disease dis : diseases){
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
