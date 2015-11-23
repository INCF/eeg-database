package cz.zcu.kiv.eegdatabase.wui.core.experimentLicense;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

/**
 * Created by Lichous on 28.4.15.
 */
public interface ExperimentLicenseFacade extends GenericFacade<ExperimentLicence, Integer> {

    void remove(Experiment experiment, License license);
    
    /**
     * List all experiment licenses for the given experiment.
     * @param experiment the experiment
     * @return list of experiment licenses
     */
    List<ExperimentLicence> getExperimentLicensesForExperiment(Experiment experiment);
    
    /**
     * Find the experiment license object for the given experiment and license.
     * @param experiment the experiment
     * @param license the license
     * @return the experiment license
     */
    ExperimentLicence getExperimentLicense(Experiment experiment, License license);
    
}
