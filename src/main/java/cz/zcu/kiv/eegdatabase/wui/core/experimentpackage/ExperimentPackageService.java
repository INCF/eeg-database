package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;
import java.util.List;

/**
 * ExperimentPackage related services.
 *
 * @author Jakub Danek
 */
public interface ExperimentPackageService extends GenericService<ExperimentPackage, Integer> {

    /**
     * Returns list of packages belonging to the research group
     * @param researchGroupId id of the research group
     * @return list of packages or an empty list
     */
    public List<ExperimentPackage> listExperimentPackagesByGroup(int researchGroupId);
	
	/**
	 * Saves into database new ExperimentPackage. 
	 * @param pack Package that should be stored into database.
	 * @param license Default license the specified pack will be published under. Can be null in case of private licenses.
	 * If the group doens't have rights to create private packages, exception will be thrown.
	 * @return 
	 */
	public Integer create(ExperimentPackage pack, License license);

}
