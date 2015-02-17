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
 *   PersonalLicenseRejectPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicenseState;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Panel with form for PersonalLicense request rejection. Allows the research group
 * admin to provide reasons for rejection to the user.
 *
 * @author Jakub Danek
 */
public class PersonalLicenseRejectPanel extends Panel {
    
	private static final long serialVersionUID = -4950246183380647386L;

    @SpringBean
	private LicenseFacade licenseFacade;

	private IModel<PersonalLicense> applicationModel;
	
	private Form form;

	public PersonalLicenseRejectPanel(String id, IModel<PersonalLicense> licenseApplication) {
		super(id);
		this.applicationModel = licenseApplication;
		form = new StatelessForm("form");
		form.setOutputMarkupId(true);
		this.add(form);
		this.add(new Label("title", ResourceUtils.getModel("heading.license.request", applicationModel)));

		this.addComponents();
		this.addControls();
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		//ugly hack to disable stupid global feedback
		((MenuPage)this.getPage()).getFeedback().setEnabled(false);
	}

	private void addComponents() {
		FormComponent c = new TextArea("resolutionComment", new PropertyModel(applicationModel, "resolutionComment"));
		c.setLabel(ResourceUtils.getModel("label.license.request.resolution"));
		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
	}

	private void addControls() {
		Button b = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				PersonalLicenseRejectPanel.this.onSubmitAction(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(form);
			}


		};
		form.add(b);

		b = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				form.clearInput();
				PersonalLicenseRejectPanel.this.onCancelAction(target);
			}

		};
		b.setDefaultFormProcessing(false);
		form.add(b);
	}

	/**
	 * Rejects the selected license application.
	 * 
	 * Override to provide additional actions.
	 * Dont forget to call super.onSubmitAction(target) to reject the application
	 * or save changes to the rejection comment.
	 */
	protected void onSubmitAction(AjaxRequestTarget target) {
		if(this.applicationModel.getObject().getLicenseState() != PersonalLicenseState.REJECTED) {
			licenseFacade.rejectRequestForLicense(this.applicationModel.getObject());
		} else {
			licenseFacade.updatePersonalLicense(this.applicationModel.getObject());
		}
	}

	/**
	 * Override to provide action on cancel button click.
	 *
	 * @param target
	 */
	protected void onCancelAction(AjaxRequestTarget target) {
		//override this
	}

}
