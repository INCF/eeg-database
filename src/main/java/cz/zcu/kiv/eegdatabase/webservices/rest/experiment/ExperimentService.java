package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.ExperimentData;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public interface ExperimentService {

    public List<ExperimentData> getAllExperiments();

    public List<ExperimentData> getMyExperiments();
}
