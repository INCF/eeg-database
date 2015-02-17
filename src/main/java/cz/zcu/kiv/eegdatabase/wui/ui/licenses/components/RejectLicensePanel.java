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
 *   RejectLicensePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
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
public class RejectLicensePanel extends Panel {

	private static final long serialVersionUID = 1687021633981394535L;

    @SpringBean
	private LicenseFacade licenseFacade;

	private ModalWindow window;

	public RejectLicensePanel(String id, IModel<PersonalLicense> request) {
		super(id);

		window = new ModalWindow("window");
		add(window);

		PersonalLicenseRejectPanel panel = new PersonalLicenseRejectPanel(window.getContentId(), request) {

			@Override
			protected void onSubmitAction(AjaxRequestTarget target) {
				super.onSubmitAction(target);
				window.close(target);
				setResponsePage(this.getPage().getPageClass(), this.getPage().getPageParameters());
			}

			@Override
			protected void onCancelAction(AjaxRequestTarget target) {
				window.close(target);
			}

		};
		window.setAutoSize(true);
		window.setResizable(false);
		window.setContent(panel);

		add(new AjaxLink<PersonalLicense>("link", request) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				window.show(target);
			}
		}.add(new Label("label", getLinkText())));
	}

	protected IModel<String> getLinkText() {
		return ResourceUtils.getModel("link.reject");
	}
}
