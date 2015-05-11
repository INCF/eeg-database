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
 *   LicenseEditForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.order.components;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author Jakub Danek
 */
public class PromoCodePopupForm extends Panel {

	private static final long serialVersionUID = -3134581180799936781L;

	private Form form;
    private RequiredTextField <String> field;

    private IModel<StringWrapper> strWrapper;

	public PromoCodePopupForm(String id, IModel<StringWrapper> model) {
		super(id);

        this.strWrapper = model;
		this.form = new Form("form");
		this.add(form);

		this.addFormFields();
		this.addControls();
	}

	private void addFormFields() {
        field = new RequiredTextField<String>("codeReturn", new PropertyModel(strWrapper, "value"));
        //field.setLabel(ResourceUtils.getModel("label.license.title"));
        //field = new RequiredTextField("code", String.class);
		//field.setLabel(ResourceUtils.getModel("label.license.title"));
		form.add(field);

		//WicketUtils.addLabelsAndFeedback(form);
	}

	/**
	 * Add window controls - buttons, etc
	 *
	 */
	private void addControls() {
		AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onSubmitAction(strWrapper, target, form);
                target.add(form);
            }

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(form);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(true);
			}
		};
		form.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancelAction(strWrapper, target, form);
				form.clearInput();
				target.add(form);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(true);
			}
		};
		button.setDefaultFormProcessing(false);
		form.add(button);

	}

    public String getCodeValue(){
        return field.getValue();
    }

    /**
     * Override this method to provide onSubmit action
     *
     * @param model license model
     * @param target ajax target
     * @param form form of the window
     */
    protected void onSubmitAction(IModel<StringWrapper> model, AjaxRequestTarget target, Form<?> form) {
    }

    /**
     * Override this method to provide onCancel action
     *
     * @param model license model
     * @param target ajax target
     * @param form form of the window
     */
    protected void onCancelAction(IModel<StringWrapper> model, AjaxRequestTarget target, Form<?> form) {
    }

}
