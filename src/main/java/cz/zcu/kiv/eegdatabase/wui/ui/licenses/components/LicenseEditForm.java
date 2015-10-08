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

import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;

import org.apache.wicket.util.time.Duration;

/**
 *
 * @author Jakub Danek
 */
public class LicenseEditForm extends Panel {

	private static final long serialVersionUID = -3134581180799936781L;

    private IModel<List<License>> blueprintsModel;
	//protected IModel<License> licenseModel;
	//protected IModel<License> selectedBlueprintModel;
	
	private Form<ExperimentPackageLicense> form;
	private boolean displayControls = true;

	private boolean displayRemoveButton = true;
    private AjaxButton saveButton;
    private NumberTextField<BigDecimal> priceInput;
    private ResourceLink<Void> downloadLink;
    private AjaxDropDownChoice<License> licenseChoice;
    
    private WebMarkupContainer licenseDetails;
    private ExternalLink licenseLink;
    //private ResourceLink<Void> downloadLink;
    

    @SpringBean
    LicenseFacade licenseFacade;

	public LicenseEditForm(String id, IModel<License> model) {
		this(id, model, null);
	}

	public LicenseEditForm(String id, IModel<License> model, IModel<List<License>> blueprints) {
		super(id);

		//this.licenseModel = model;
		blueprintsModel = blueprints;

		this.form = new Form<ExperimentPackageLicense>("form");
		ExperimentPackageLicense modelObj = new ExperimentPackageLicense();
		//modelObj.setExperimentPackage(experimentPackage);
		modelObj.setPrice(BigDecimal.ZERO);
		form.setModel(new CompoundPropertyModel<ExperimentPackageLicense>(modelObj));
		this.add(form);

		this.addFormFields();
		//this.addBlueprintSelect();
		this.addControls();
		this.addLicenseDetails();
	}

	private void addFormFields() {
		priceInput = new NumberTextField<BigDecimal>("price");
		priceInput.setMinimum(BigDecimal.ZERO);
		priceInput.setRequired(true);
        priceInput.setEnabled(false);
		priceInput.setLabel(ResourceUtils.getModel("label.license.price"));
		form.add(priceInput);

		WicketUtils.addLabelsAndFeedback(form);
		
		addBlueprintSelect();
	}

	/**
	 * Add window controls - buttons, etc
	 *
	 * @param cont
	 */
	@SuppressWarnings("serial")
    private void addControls() {
		saveButton = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSubmitAction(LicenseEditForm.this.form.getModel(), target, form);
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
        saveButton.setVisibilityAllowed(false);
		form.add(saveButton);
		
        AjaxButton button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancelAction(LicenseEditForm.this.form.getModel(), target, form);
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

		/*button = new AjaxButton("removeButton", ResourceUtils.getModel("button.remove")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onRemoveAction(LicenseEditForm.this.form.getModel(), target, form);
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				this.setVisible(displayControls && displayRemoveButton);
			}
		};
		form.add(button);*/
	}
	
	
	private void addLicenseDetails() {
        licenseDetails = new WebMarkupContainer("licenseDetails");
        licenseDetails.setVisible(false);
        form.add(licenseDetails);
        
        Label title = new Label("title", new PropertyModel<String>(form.getModel(), "license.title"));
        licenseDetails.add(title);
        
        Label type = new Label("type", new PropertyModel<String>(form.getModel(), "license.licenseType"));
        licenseDetails.add(type);
        
        Label description = new Label("description", new PropertyModel<String>(form.getModel(), "license.description"));
        licenseDetails.add(description);
        
        PropertyModel<String> linkModel = new PropertyModel<String>(form.getModel(), "license.link");
        licenseLink = new ExternalLink("link", linkModel, linkModel);
        licenseDetails.add(licenseLink);
        
        downloadLink = new ResourceLink<Void>("fileDownload", (IResource) null);
        downloadLink.setVisible(false);
        Label fileName = new Label("fileName", new PropertyModel<String>(form.getModel(), "license.attachmentFileName"));
        downloadLink.add(fileName);
        licenseDetails.add(downloadLink);
    }
	

	@SuppressWarnings("serial")
    private void addBlueprintSelect() {
	    IModel<License> selectedBlueprintModel = new Model<License>();
	    
	    licenseChoice = new AjaxDropDownChoice<License>("blueprintSelect", selectedBlueprintModel, blueprintsModel, new ChoiceRenderer<License>("title")) {

            @Override
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, final License option) {
				if (option == null || option.getLicenseId() == 0) {
                    form.getModelObject().setLicense(null);
                    form.getModelObject().setPrice(BigDecimal.ZERO);
                    saveButton.setVisibilityAllowed(false);
                    priceInput.setEnabled(false);
                    licenseDetails.setVisible(false);
				} else {
				    priceInput.setEnabled(option.getLicenseType() == LicenseType.COMMERCIAL);
                    licenseDetails.setVisible(true);
                    licenseLink.setVisible(option.getLink() != null);

                    if (option.getAttachmentFileName() != null) {
                        ByteArrayResource res;
                        res = new ByteArrayResource("", licenseFacade.getLicenseAttachmentContent(option.getLicenseId()), option.getAttachmentFileName()) {
                            @Override
                            public void configureResponse(final AbstractResource.ResourceResponse response, final IResource.Attributes attributes) {
                                response.setCacheDuration(Duration.NONE);
                                response.setFileName(option.getAttachmentFileName());
                            }
                        };
                        ResourceLink<Void> newLink = new ResourceLink<Void>("fileDownload", res);
                        newLink.add(new Label("fileName", option.getAttachmentFileName()));
                        downloadLink.replaceWith(newLink);
                        downloadLink = newLink;
                        downloadLink.setVisible(true);
                    } else {
                        downloadLink.setVisible(false);
                    }
                    
                    form.getModelObject().setLicense(option);
                    saveButton.setVisibilityAllowed(true);
				}
				target.add(form);
			}
		};

		licenseChoice.setNullValid(true);
		form.add(licenseChoice);
	}

	
    public void clearLicenseModel() {
        licenseChoice.setDefaultModelObject(null);
        downloadLink.setVisibilityAllowed(false);
        saveButton.setVisibilityAllowed(false);
    }

    /*public void setLicenseModel (License license) {
        licenseModel.setObject(license);
    }*/

	public boolean isDisplayControls() {
		return displayControls;
	}

	public void setDisplayControls(boolean displayControls) {
		this.displayControls = displayControls;
	}

    public void setSaveButtonVisibility(boolean visibility) {
        saveButton.setVisibilityAllowed(visibility);
    }

	public boolean isDisplayRemoveButton() {
		return displayRemoveButton;
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
	protected void onSubmitAction(IModel<ExperimentPackageLicense> model, AjaxRequestTarget target, Form<?> form) {
	}

	
	/**
	 * Override this method to provide onCancel action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onCancelAction(IModel<ExperimentPackageLicense> model, AjaxRequestTarget target, Form<?> form) {
	}

	
	/**
	 * Override this method to provide onRemove action
	 *
	 * @param model license model
	 * @param target ajax target
	 * @param form form of the window
	 */
	protected void onRemoveAction(IModel<ExperimentPackageLicense> model, AjaxRequestTarget target, Form<?> form) {
	}
	
}
