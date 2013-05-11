package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
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
    public List<ExperimentPackage> listExperimentPackagesByGroup(ResearchGroup researchGroup);

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

    /**
     * Removes an experiment from the package
     * @param exp experiment to be removed
     * @param pckg package to remove from
     * @return true if success, false otherwise (e.g. package is not there)
     */
    public boolean removeExperimentFromPackage(Experiment exp, ExperimentPackage pckg);

    /**
     * Removes multiple experiments from the package
     * @param exp experiments to be removed
     * @param pckg package to remove from
     * @return number of experiments removed
     */
    public int removeExperimentsFromPackage(List<Experiment> exp, ExperimentPackage pckg);
	
	/**
     * Creates new package
     * @param pckg package 
	 * @param license license
     */
    public void createExperimentPackage(ExperimentPackage pckg, License license);

	/**
	 * Removes experiment package from research group. Erases all experiment and
	 * license connections bound to the package
	 * @param pckg the package to be removed
	 */
	public void removeExperimentPackage(ExperimentPackage pckg);

}
