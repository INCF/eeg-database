package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
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
}
