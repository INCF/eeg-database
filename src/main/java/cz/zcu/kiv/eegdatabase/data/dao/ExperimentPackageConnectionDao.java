/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ExperimentPackageConnectionDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
