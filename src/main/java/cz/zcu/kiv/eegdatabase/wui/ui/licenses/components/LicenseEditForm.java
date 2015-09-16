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

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import org.apache.wicket.util.time.Duration;

import javax.sql.rowset.serial.SerialBlob;

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
    private AjaxButton saveButton;
    private FormComponent priceInput;
    private ResourceLink<Void> downloadLink;
    private AjaxDropDownChoice<License> ddc;

    @SpringBean
    LicenseFacade licenseFacade;

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
        c.setEnabled(false);
		form.add(c);

        Label l = new Label("attachmentFileName", new PropertyModel(licenseModel, "attachmentFileName"));
        form.add(l);

		c = new TextArea("description", new PropertyModel(licenseModel, "description"));
		c.setLabel(ResourceUtils.getModel("label.license.description"));
		c.setRequired(true);
        c.setEnabled(false);
		form.add(c);

		priceInput = new NumberTextField<BigDecimal>("price", new PropertyModel(licenseModel, "price"), BigDecimal.class).setMinimum(BigDecimal.ZERO);
		priceInput.setRequired(true);
        priceInput.setEnabled(false);
		priceInput.setLabel(ResourceUtils.getModel("label.license.price"));
		form.add(priceInput);

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
        c.add(new Radio("public",new Model(LicenseType.OPEN_DOMAIN)));
        c.setEnabled(false);
		form.add(c);

		WicketUtils.addLabelsAndFeedback(form);

        ByteArrayResource res;
        res = new ByteArrayResource("") {
            @Override
            public void configureResponse(final AbstractResource.ResourceResponse response, final IResource.Attributes attributes) {
                response.setCacheDuration(Duration.NONE);
            }
        };
        downloadLink = new ResourceLink<Void>("download", res);
        downloadLink.setVisible(false);

        form.add(downloadLink);


	}

	/**
	 * Add window controls - buttons, etc
	 *
	 * @param cont
	 */
	private void addControls() {
		saveButton = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
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
        saveButton.setVisibilityAllowed(false);
		form.add(saveButton);
        AjaxButton button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
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
		ddc = new AjaxDropDownChoice<License>("blueprintSelect", selectedBlueprintModel, blueprintsModel, new ChoiceRenderer<License>("title")) {
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
			protected void onSelectionChangeAjaxified(AjaxRequestTarget target, final License option) {
				if (option == null || option.getLicenseId() == 0) {
					licenseModel.setObject(new License());
                    saveButton.setVisibilityAllowed(false);
                    priceInput.setEnabled(false);
                    downloadLink.setVisible(false);


				} else {
				    // TODO kuba licence
                    if (option.getLicenseType()== LicenseType.BUSINESS /*&& option.getPrice().intValue() == 0*/) {
                        priceInput.setEnabled(true);
                    } else {
                        priceInput.setEnabled(false);
                    }
					final License l = new License();
					l.setTitle(option.getTitle());
					l.setDescription(option.getDescription());
					l.setLicenseType(option.getLicenseType());
					//l.setPrice(option.getPrice());
                    l.setAttachmentFileName(option.getAttachmentFileName());
                    l.setFileContentStream(null);


                    if (option.getAttachmentFileName()!= null) {

                        //--------
                        try {
                            Blob blob = null;
                            blob = new SerialBlob(licenseFacade.getLicenseAttachmentContent(option.getLicenseId()));
                            l.setAttachmentContent(blob);

                            ByteArrayResource res;
                            res = new ByteArrayResource("", blob.getBytes(1,(int)blob.length())){
                                @Override
                                public void configureResponse(final AbstractResource.ResourceResponse response, final IResource.Attributes attributes) {
                                    response.setCacheDuration(Duration.NONE);
                                    response.setFileName(option.getAttachmentFileName());
                                }
                            };

                            ResourceLink<Void> newLink = new ResourceLink<Void>("download", res);

                            downloadLink = (ResourceLink<Void>) downloadLink.replaceWith(newLink);
                            downloadLink.setVisible(true);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    } else {
                        downloadLink.setVisible(false);
                    }
                    licenseModel.setObject(l);
                    saveButton.setVisibilityAllowed(true);
				}
				target.add(form);
			}
		};

		ddc.setNullValid(true);


		form.add(ddc);
	}

    public void clearLicenseModel() {
        ddc.setDefaultModelObject(null);
        downloadLink.setVisibilityAllowed(false);
        saveButton.setVisibilityAllowed(false);
    }

    public void setLicenseModel (License license) {
        licenseModel.setObject(license);
    }

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
