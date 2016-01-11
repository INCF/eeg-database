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
 *   ListExperimentsDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Dataprovider implementation used in table on ListExperimentPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListExperimentsDataProvider extends BasicDataProvider<Experiment> {

    private static final long serialVersionUID = 2580741571579801137L;

    /**
     * Specialized constructor which loads experiments based on given properties
     * @param facade ExperimentsFacade which will be used to load experiments
     * @param person person object used for filtering
     * @param owner search by isOwner (related to the person parameter)
     * @param subject  search by isSubject (related to the person parameter)
     */
    public ListExperimentsDataProvider(ExperimentsFacade facade, Person person, boolean owner, boolean subject) {
		super("experimentId", SortOrder.ASCENDING);
		List<Experiment> list;

		int size;
			if (owner) {
				size = facade.getCountForExperimentsWhereOwnerOrExperimenter(person);
				list = facade.getMyExperiments(person, (int) 0, size);
			} else if (subject) {
				size = facade.getCountForExperimentsWhereSubject(person);
				list = facade.getExperimentsWhereSubject(person, (int) 0, size);
			} else {
				size = facade.getCountForAllExperimentsForUser(person);
				list = facade.getAllExperimentsForUser(person, (int) 0, size);
			}

		super.listModel.setObject(list);

    }

	/**
     * Constructor which wraps a list of experiments.
     * @param experiments
     */
    public ListExperimentsDataProvider(List<Experiment> experiments) {
		super(experiments, "experimentId", SortOrder.ASCENDING);
    }

    /**
     * Constructor which allows user to provide his own list model. Suitable
     * when you need to update the model from the outside (e.g. with ajax and
     * LoadableDetachableModel).
     * @param listModel
     */
    public ListExperimentsDataProvider(IModel<List<Experiment>> listModel) {
		super(listModel, "experimentId", SortOrder.ASCENDING);
    }

}
