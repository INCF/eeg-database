package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.util.List;

/**
 * ExperimentPackage related services.
 *
 * @author Jakub Danek
 */
public interface ExperimentPackageService extends GenericService<ExperimentPackage, Integer> {

    /**
     * Returns list of packages belonging to the research groub
     * @param researchGroupId id of the research group
     * @return list of packages or an empty list
     */
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId);

}
