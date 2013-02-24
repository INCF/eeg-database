package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.ExperimentData;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Service
public class ScenarioServiceImpl implements ScenarioService {

    @Autowired
    @Qualifier("scenarioDao")
    private ScenarioDao scenarioDao;
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;

    private final Comparator<ScenarioData> idComparator = new Comparator<ScenarioData>() {
        @Override
        public int compare(ScenarioData o1, ScenarioData o2) {
            return o1.getScenarioId() - o2.getScenarioId();
        }
    };

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioData> getAllScenarios() {
        List<Scenario> scenarios = scenarioDao.getAllRecords();
        List<ScenarioData> scenarioDatas = new ArrayList<ScenarioData>(scenarios.size());

        for (Scenario s : scenarios) {
            ScenarioData sd = new ScenarioData();
            Person owner = s.getPerson();
            sd.setScenarioId(s.getScenarioId());
            sd.setScenarioName(s.getTitle());
            sd.setOwnerName(owner.getGivenname() + " " + owner.getSurname());
            sd.setMimeType(s.getMimetype());
            sd.setFileName(s.getScenarioName());
            sd.setFileLength(s.getScenarioLength());
            sd.setDescription(s.getDescription());
            sd.setPrivate(s.isPrivateScenario());
            sd.setResearchGroupName(s.getResearchGroup().getTitle());
            scenarioDatas.add(sd);
        }

        Collections.sort(scenarioDatas, idComparator);
        return scenarioDatas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioData> getMyScenarios() {
        List<Scenario> scenarios = scenarioDao.getScenariosWhereOwner(personDao.getLoggedPerson());
        List<ScenarioData> scenarioDatas = new ArrayList<ScenarioData>(scenarios.size());

        for (Scenario s : scenarios) {
            ScenarioData sd = new ScenarioData();
            Person owner = s.getPerson();
            sd.setScenarioId(s.getScenarioId());
            sd.setScenarioName(s.getTitle());
            sd.setOwnerName(owner.getGivenname() + " " + owner.getSurname());
            sd.setMimeType(s.getMimetype());
            sd.setFileName(s.getScenarioName());
            sd.setFileLength(s.getScenarioLength());
            sd.setDescription(s.getDescription());
            sd.setPrivate(s.isPrivateScenario());
            sd.setResearchGroupName(s.getResearchGroup().getTitle());
            scenarioDatas.add(sd);
        }
        Collections.sort(scenarioDatas, idComparator);
        return scenarioDatas;
    }
}
