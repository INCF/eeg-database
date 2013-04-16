package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import java.util.List;

/**
 * Facade for experimentpackage related services.
 *
 * @author Jakub Danek
 */
public interface ExperimentPackageFacade extends GenericFacade<ExperimentPackage, Integer> {

    /**
     * Returns list of packages belonging to the research groub
     * @param researchGroupId id of the research group
     * @return list of packages or an empty list
     */
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId);

    /**
     * Adds a single experiment to the package.
     * @param exp experiment to be added
     * @param pckg package
     * @return true if success, false if not (e.g. experiment already in the package)
     */
    public boolean addExperimentToPackage(Experiment exp, ExperimentPackage pckg);

    /**
     * Adds experiments to the package.
     * @param exp experiments to be added
     * @param pckg package
     * @return number of experiments successfully added
     */
    public int addExperimentsToPackage(List<Experiment> exp, ExperimentPackage pckg);

}
