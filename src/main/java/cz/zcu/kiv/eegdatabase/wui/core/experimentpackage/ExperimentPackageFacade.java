package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

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

}
