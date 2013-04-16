package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

/**
 * Services related to connections between experiment packages and experiments.
 *
 * @author Jakub Danek
 */
public interface ExperimentPackageConnectionService  extends GenericService<ExperimentPackageConnection, Integer> {


    /**
     * Adds a single experiment to the package.
     * @param exp experiment to be added
     * @param pckg package
     * @return true if success, false if not (e.g. experiment already in the package)
     */
    public boolean addExperimentToPackage(Experiment exp, ExperimentPackage pckg);
}
