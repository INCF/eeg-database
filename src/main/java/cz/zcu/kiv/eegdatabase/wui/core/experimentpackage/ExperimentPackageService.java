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
 *   ExperimentPackageService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
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
	 * Returns list of visible packages for a person.
	 *
	 * That is all but private (except the user has owner license).
	 * @return
	 */
	public List<ExperimentPackage> listVisiblePackages(Person person);

}
