package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.ExperimentData;
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

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("experimentDao")
    private ExperimentDao experimentDao;

    private final Comparator<ExperimentData> idComparator = new Comparator<ExperimentData>() {
        @Override
        public int compare(ExperimentData id1, ExperimentData id2) {
            return id1.getExperimentId() - id2.getExperimentId();
        }
    };

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentData> getAllExperiments() {
        List<Experiment> exps = experimentDao.getAllRecords();
        List<ExperimentData> experiments = new ArrayList<ExperimentData>(exps.size());

        for (Experiment exp : exps) {
            ExperimentData expData = new ExperimentData();
            expData.setExperimentId(exp.getExperimentId());
            expData.setStartTime(exp.getStartTime());
            expData.setEndTime(exp.getEndTime());
            expData.setScenarioId(exp.getScenario().getScenarioId());
            expData.setScenarioName(exp.getScenario().getTitle());
            experiments.add(expData);
        }

        Collections.sort(experiments, idComparator);
        return experiments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentData> getMyExperiments() {
        Person loggedUser = personDao.getLoggedPerson();
        Collection<Experiment> exps = loggedUser.getExperimentsForOwnerId();
        List<ExperimentData> experiments = new ArrayList<ExperimentData>(exps.size());

        for (Experiment exp : exps) {
            ExperimentData expData = new ExperimentData();
            expData.setExperimentId(exp.getExperimentId());
            expData.setStartTime(exp.getStartTime());
            expData.setEndTime(exp.getEndTime());
            expData.setScenarioId(exp.getScenario().getScenarioId());
            expData.setScenarioName(exp.getScenario().getScenarioName());
            experiments.add(expData);
        }
        Collections.sort(experiments, idComparator);
        return experiments;
    }
}
