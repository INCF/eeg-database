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
 *   PersonalLicenseListPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Panel with table of personal licenses.
 *
 * @author Jakub Danek
 */
public class PersonalLicenseListPanel extends Panel {

	private static final long serialVersionUID = 3469818705264835133L;

    private static final int REQUESTS_PER_PAGE = 50;

	private IModel<List<PersonalLicense>> requestModel;

	private WebMarkupContainer cont;

	public PersonalLicenseListPanel(String id, IModel<List<PersonalLicense>> requestsModel) {
		super(id);
		this.requestModel = requestsModel;
		cont = new WebMarkupContainer("cont");
		cont.setOutputMarkupId(true);
		this.add(cont);
		this.addPersonalLicenseListToCont(cont);
		this.setOutputMarkupId(true);
	}

	/**
     * Add view for the list of experiments to a container given
     * @param cont the container
     */
    private void addPersonalLicenseListToCont(WebMarkupContainer cont) {
		DataTable<PersonalLicense, String> list = new AjaxFallbackDefaultDataTable<PersonalLicense, String>("list", createListColumns(),
								new BasicDataProvider<PersonalLicense>(requestModel, "personalLicenseId", SortOrder.ASCENDING), REQUESTS_PER_PAGE);

		cont.add(list);
    }

    /**
     *
     * @return list of columns the table of experiments shall display
     */
    private List<? extends IColumn<PersonalLicense, String>> createListColumns() {
		List<IColumn<PersonalLicense, String>> columns = new ArrayList<IColumn<PersonalLicense, String>>();

        columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.number"), "personalLicenseId", "personalLicenseId"));
        columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.licenseTitle"), "license.title", "license.title"));
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.person"), "fullName", "fullName"));
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.organisation"), "organisation", "organisation"));
        columns.add(new TimestampPropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.requestedDate"), "requestedDate", "requestedDate", "dd-MM-yyyy"));
		columns.add(new PropertyColumn<PersonalLicense, String>(ResourceUtils.getModel("dataTable.heading.downloads"), null, null) {
            @Override
            public void populateItem(Item<ICellPopulator<PersonalLicense>> item, String componentId, IModel<PersonalLicense> rowModel) {
                item.add(new DownloadRequestAttachmentPanel(componentId, rowModel));
            }
        });
		this.addListColumns(columns);

		return columns;
    }

	/**
	 * Override this method to add aditional columns to the table which displays
	 * list of personal licenses.
	 * @param columns
	 */
	protected void addListColumns(List<IColumn<PersonalLicense, String>> columns) {

	}

}
