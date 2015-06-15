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
 *   ExperimentPackageListPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.CustomAjaxPagingNavigator;

import java.util.List;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

/**
 * Panel which displays list of experiment packages and experiments
 * they contain.
 *
 * @author Jakub Danek
 */
public class ExperimentPackageListPanel extends Panel {

    private static final int ITEMS_PER_PAGE = 10;

    /**
     * Main model - list of experiment packages
     */
    private IModel<List<ExperimentPackage>> epListModel;

	private final boolean managedMode;

    /**
	 * Constructor for displaying list of packages in view mode.
     *
     * @param id components id
     * @param model model with packages that shall be displayed
     */
    public ExperimentPackageListPanel(String id, IModel<List<ExperimentPackage>> model) {
		this(id, model, false);
    }

	/**
	 * 
	 * @param id
	 * @param model
	 * @param manageMode
	 */
	public ExperimentPackageListPanel(String id, IModel<List<ExperimentPackage>> model, boolean manageMode) {
		super(id);

		this.setOutputMarkupId(true);
		this.managedMode = manageMode;
		this.epListModel = model;
		this.addPackageList();
	}

    /**
     * Add view for the list of packages.
     */
    private void addPackageList() {
		PageableListView<ExperimentPackage> listView = new PageableListView<ExperimentPackage>("packageList", epListModel, ITEMS_PER_PAGE) {

			@Override
			protected void populateItem(ListItem<ExperimentPackage> item) {
				if(managedMode) {
					item.add(new ExperimentPackageManagePanel("item", item.getModel()));
				} else {
					item.add(new ExperimentPackagePanel("item", item.getModel()));
				}
			}
		};

		listView.setOutputMarkupId(true);

		add(listView);
		PagingNavigator navig = new BootstrapPagingNavigator("navigation", listView);
		if (listView.getPageCount() < 2)
		    navig.setVisible(false);
		add(navig);
    }

}
