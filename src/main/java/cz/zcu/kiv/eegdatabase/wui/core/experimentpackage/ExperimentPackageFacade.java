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
 *   ExperimentPackageFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
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
	 * Returns list of visible packages for a person.
	 *
	 * That is all but private (except the user has owner license).
	 * @return
	 */
	public List<ExperimentPackage> listVisiblePackages(Person person);

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
	 * Removes experiment package from research group. Erases all experiment and
	 * license connections bound to the package
	 * @param pckg the package to be removed
	 */
	public void removeExperimentPackage(ExperimentPackage pckg);

}
