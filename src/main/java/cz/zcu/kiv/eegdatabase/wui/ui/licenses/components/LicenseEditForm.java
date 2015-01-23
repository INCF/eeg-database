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
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;

/**
 *
 * @author Jakub Danek
 */
public class LicenseEditForm extends Panel {

	private static final long serialVersionUID = -3134581180799936781L;
	
    private IModel<List<License>> blueprintsModel;
	protected IModel<License> licenseModel;
	protected IModel<License> selectedBlueprintModel;
	private Form form;
	private boolean displayControls = true;
	private IModel<Boolean> allowBusiness;
	private boolean displayRemoveButton = true;
    private FileUploadField fileUpload;

	public LicenseEditForm(String id, IModel<License> model, IModel<Boolean> allowBusiness) {
		this(id, model, null, allowBusiness);
	}

	public LicenseEditForm(String id, IModel<License> model, IModel<List<License>> blueprints, IModel<Boolean> allowBusiness) {
		super(id);

		this.allowBusiness = allowBusiness;

		this.licenseModel = model;
		blueprintsModel = blueprints;

		this.form = new Form("form");
		this.add(form);

		this.addFormFields();
		this.addBlueprintSelect();
		this.addControls();
	}

	private void addFormFields() {
		FormComponent c = new RequiredTextField("title", new PropertyModel(licenseModel, "title"));
		c.setLabel(ResourceUtils.getModel("label.license.title"));
		form.add(c);

		c = new TextArea("description", new PropertyModel(licenseModel, "description"));
		c.setLabel(ResourceUtils.getModel("label.license.description"));
		c.setRequired(true);
		form.add(c);

		c = new NumberTextField<BigDecimal>("price", new PropertyModel(licenseModel, "price"), BigDecimal.class).setMinimum(BigDecimal.ZERO);
		c.setRequired(true);
		c.setLabel(ResourceUtils.getModel("label.license.price"));
		form.add(c);

		c = new RadioGroup<LicenseType>("licenseType", new PropertyModel<LicenseType>(licenseModel, "licenseType"));
		c.setLabel(ResourceUtils.getModel("label.license.type"));
		c.setRequired(true);
		c.add(new Radio("academic", new Model(LicenseType.ACADEMIC)));
		c.add(new Radio("business", new Model(LicenseType.BUSINESS)) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(allowBusiness.getObject());
			}
		});
		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);
		
		fileUpload = new FileUploadField("licenseFile");
		fileUpload.setLabel(ResourceUtils.getModel("label.license.attachment.file"));
		
		form.add(fileUpload);
		form.setMaxSize(Bytes.megabytes(15));
	}

	/**
	 * Add window controls - buttons, etc
	 *
	 * @param cont
	 */
	private void addControls() {
		AjaxButton button = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitAction(licenseModel, target, form);
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
				this.setVisible(displayControls);
			}
		};
		form.add(button);

		button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancelAction(licenseModel, target, form);
				form.clearInput();
				target.add(form);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(displayControls);
			}
		};
		button.setDefaultFormProcessing(false);
		form.add(button);

		button = new AjaxButton("removeButton", ResourceUtils.getModel("button.remove")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onRemoveAction(licenseModel, target, form);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(displayControls && displayRemoveButton);
			}
		};
		form.add(button);
	}

	private void addBlueprintSelect() {
		selectedBlueprintModel = new Model<License>();
		AjaxDropDownChoice<License> ddc = new AjaxDropDownChoice<License>("blueprintSelect", selectedBlueprintModel, blueprintsModel, new ChoiceRenderer<License>("title")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				boolean viz = false;
				if (blueprintsModel != null) {
					viz = blueprintsModel.getObject() != null ? !blueprintsModel.getObject().isEmpty() : false;
				}
				if (licenseModel.getObject() != null && licenseModel.getObject().getLicenseId() != 0) {
					viz = false;
				}
				this.setVisible(viz);
			}

			@Override
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
				if (option == null || option.getLicenseId() == 0) {
					licenseModel.setObject(new License());
				} else {
					License l = new License();
					l.setTitle(option.getTitle());
					l.setDescription(option.getDescription());
					l.setLicenseType(option.getLicenseType());
					l.setPrice(option.getPrice());
					licenseModel.setObject(l);
				}
				target.add(form);
			}
		};

		ddc.setNullValid(true);
		form.add(ddc);
	}

	public boolean isDisplayControls() {
		return displayControls;
	}

	public void setDisplayControls(boolean displayControls) {
		this.displayControls = displayControls;
	}

	public boolean isDisplayRemoveButton() {
		return displayRemoveButton;
	}
	
	public FileUploadField getFileUpload() {
        return fileUpload;
    }

	public LicenseEditForm setDisplayRemoveButton(boolean displayRemoveButton) {
		this.displayRemoveButton = displayRemoveButton;
		return this;
	}

	/**
	 * Override this method to provide onSubmit action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onSubmitAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
	}

	/**
	 * Override this method to provide onCancel action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onCancelAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
	}

	/**
	 * Override this method to provide onRemove action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onRemoveAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
	}
}
