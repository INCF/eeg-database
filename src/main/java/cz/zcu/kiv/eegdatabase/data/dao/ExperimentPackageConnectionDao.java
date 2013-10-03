package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import java.util.List;

/**
 * Dao for managing connections between experiments and packages.
 *
 * @author bydga
 */
public interface ExperimentPackageConnectionDao extends GenericDao<ExperimentPackageConnection, Integer> {

    /**
     * Returns list of experiments belonging to the given package.
     * @param packageId id of the package
     * @return list of experiments or an empty list
     */
    public List<Experiment> listExperimentsByPackage(int packageId);
    /**
     * Returns list of experiments that are not members of the given package.
     * @param packageId id of the package
     * @return list of experiments or empty list
     */
    List<Experiment> listExperimentsWithoutPackage(int researchGroupId, int packageId);

    /**
     * Checks whether the experiments has been already connected to a package.
     * @param experimentId id of the experiment
     * @param packageId id of the package
     * @return true if experiment is member of the package
     */
    public boolean isExperimentInPackage(int experimentId, int packageId);
	
    public boolean removeExperimentFromPackage(int experimentId, int packageId);
	
	public void removeAllConnections(ExperimentPackage pack);

	/**
	 * List all experiments that arent in any package.
	 * @return
	 */
	public List<Experiment> listExperimentsWithoutPackage();

}
