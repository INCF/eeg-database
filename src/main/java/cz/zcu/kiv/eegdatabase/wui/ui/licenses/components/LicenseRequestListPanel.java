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
 *   LicenseRequestListPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Extends PersonalLicenseListPanel with confirm/request links for each table row.
 *
 * Usable for list of personal license requests.
 *
 * @author Jakub Danek
 */
public class LicenseRequestListPanel extends PersonalLicenseListPanel {

	private static final long serialVersionUID = -3317292731775090580L;

    public LicenseRequestListPanel(String id, IModel<List<PersonalLicense>> requestsModel) {
		super(id, requestsModel);
	}

	@Override
	protected void addListColumns(List<IColumn<PersonalLicense, String>> columns) {
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.approve"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PersonalLicense>> item, String componentId, IModel<PersonalLicense> rowModel) {
                item.add(new ConfirmLicensePanel(componentId, rowModel));
            }
        });
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.reject"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PersonalLicense>> item, String componentId, IModel<PersonalLicense> rowModel) {
                item.add(new RejectLicensePanel(componentId, rowModel));
            }
        });
	}
	
}
