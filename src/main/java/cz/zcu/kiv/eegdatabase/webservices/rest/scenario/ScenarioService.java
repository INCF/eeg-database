package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public interface ScenarioService {

    public List<ScenarioData> getAllScenarios();

    public List<ScenarioData> getMyScenarios();
}
