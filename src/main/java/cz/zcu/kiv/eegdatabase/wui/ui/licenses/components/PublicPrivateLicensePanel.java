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
 *   PublicPrivateLicensePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel with logic and texts for changing package licensing from private to
 * public and vice versa.
 *
 * @author Jakub Danek
 */
public class PublicPrivateLicensePanel extends Panel {
    
	private static final long serialVersionUID = -5439949824097279835L;

    @SpringBean
	private LicenseFacade licenseFacade;

	private IModel<ExperimentPackage> epmodel;

	private boolean isPrivate;

	private Form form;
	
	/**
	 * 
	 * @param id
	 * @param pckg 
	 * @param isPrivate
	 */
	public PublicPrivateLicensePanel(String id, IModel<ExperimentPackage> pckg, boolean isPrivate) {
		super(id);
		this.epmodel = pckg;
		this.isPrivate = isPrivate;

		this.form = new Form("form");
		this.add(form);

		this.addText();
		this.addControls();
	}

	private void addText() {
		String key = isPrivate ? "text.license.add.public" : "text.license.remove.public";
		form.add(new Label("text", ResourceUtils.getModel(key)));
	}

	private void addControls() {
		String submitKey = isPrivate ? "button.add.publicLicense" : "button.remove.publicLicense";

		AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel(submitKey)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitAction(target, form);
			}
		};
		form.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancelAction(target, form);
			}
		};
		form.add(button);
	}

	/**
	 * Override this method to provide additional onSubmit action
	 *
	 * On default removes public license if isPrivate is false or adds it otherwise.
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onSubmitAction(AjaxRequestTarget target, Form<?> form) {
		License p = licenseFacade.getPublicLicense();
		if(isPrivate) {	
			licenseFacade.addLicenseForPackage(p, epmodel.getObject());
		} else {
			licenseFacade.removeLicenseFromPackage(p, epmodel.getObject());
		}
	}

	/**
	 * Override this method to provide onCancel action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onCancelAction(AjaxRequestTarget target, Form<?> form) {
	}

}
