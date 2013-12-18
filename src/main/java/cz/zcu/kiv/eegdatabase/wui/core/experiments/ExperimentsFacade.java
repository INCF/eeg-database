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
 *   ExperimentsFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface ExperimentsFacade extends GenericFacade<Experiment, Integer> {

    Experiment getExperimentForDetail(int experimentId);

    List<DataFile> getDataFilesWhereExpId(int experimentId);

    List<DataFile> getDataFilesWhereId(int dataFileId);

    List<Experiment> getAllExperimentsForUser(Person person, int start, int count);

    List<Experiment> getExperimentsWhereOwner(Person person, int limit);

    List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit);

    List<Experiment> getExperimentsWhereSubject(Person person, int limit);

    List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit);

    int getCountForExperimentsWhereOwner(Person loggedUser);

    int getCountForExperimentsWhereSubject(Person person);

    int getCountForAllExperimentsForUser(Person person);

    List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId);

    /**
     * Returns list of experiments belonging to the package.
     * @param packageId id of the package
     * @return list of experiments or empty list
     */
    List<Experiment> getExperimentsByPackage(int packageId);

    /**
     * Returns list of experiments that are not members of the given package.
     * @param packageId id of the package
     * @return list of experiments or empty list
     */
    List<Experiment> getExperimentsWithoutPackage(ExperimentPackage pckg);

	/**
	 * List all experiments that arent in any package.
	 * @return 
	 */
	List<Experiment> getExperimentsWithoutPackage();
}
