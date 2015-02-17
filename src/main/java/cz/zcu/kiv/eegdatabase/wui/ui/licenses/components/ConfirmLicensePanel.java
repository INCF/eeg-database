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
 *   ConfirmLicensePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel with confirm link for the table (or any repeater) of PersonalLicense objects.
 *
 * The link confirms the license request.
 *
 * @author Jakub Danek
 */
public class ConfirmLicensePanel extends Panel {

	private static final long serialVersionUID = -4840427190700721562L;
    @SpringBean
	private LicenseFacade licenseFacade;

	public ConfirmLicensePanel(String id, IModel<PersonalLicense> request) {
		super(id);

		add(new Link<PersonalLicense>("link", request) {

			private static final long serialVersionUID = 1L;

            @Override
			public void onClick() {
				licenseFacade.confirmRequestForLicense(this.getModelObject());
				setResponsePage(this.getPage().getPageClass(), this.getPage().getPageParameters());
			}
		});
	}
}
