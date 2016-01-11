/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * AddLicensePriceForm.java, 8. 10. 2015 13:30:37, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import java.math.BigDecimal;
import java.util.List;

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
import org.apache.wicket.util.time.Duration;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;

/**
 * @author Jakub Krauz
 *
 */
public abstract class LicensePriceForm extends Panel {
    
    private static final long serialVersionUID = -6596032460577221156L;

    @SpringBean
    LicenseFacade licenseFacade;
    
    private Form<License> form;
    
    // form components
    private AjaxDropDownChoice<License> dropDownChoice;
    private NumberTextField<BigDecimal> priceInput;
    private AjaxButton saveButton;
    
    // license details
    private WebMarkupContainer licenseDetails;
    private ExternalLink licenseLink;
    private ResourceLink<Void> downloadLink;
    
    
    public LicensePriceForm(String id, IModel<List<License>> licenseChoices) {
        this(id, licenseChoices, null);
    }
    
    
    public LicensePriceForm(String id, IModel<List<License>> licenseChoices, IModel<License> model) {
        super(id);
        form = new Form<License>("form", new CompoundPropertyModel<License>(model));
        add(form);
        
        addFormFields();
        addDropDownChoice(licenseChoices);
        addControls();
        addLicenseDetails();
    }
    
    
    protected abstract void onSubmitAction(License license, BigDecimal price, AjaxRequestTarget target, Form<?> form);
    
    
    protected abstract void onCancelAction(AjaxRequestTarget target, Form<?> form);


    private void addFormFields() {
        priceInput = new NumberTextField<BigDecimal>("price", new Model<BigDecimal>(BigDecimal.ZERO));
        priceInput.setMinimum(BigDecimal.ZERO);
        priceInput.setRequired(true);
        priceInput.setEnabled(false);
        priceInput.setLabel(ResourceUtils.getModel("label.license.price"));
        form.add(priceInput);

        WicketUtils.addLabelsAndFeedback(form);
    }
    
    
    private void addLicenseDetails() {
        licenseDetails = new WebMarkupContainer("licenseDetails");
        licenseDetails.setVisible(false);
        form.add(licenseDetails);
        
        Label title = new Label("title");
        licenseDetails.add(title);
        
        Label type = new Label("licenseType");
        licenseDetails.add(type);
        
        Label description = new Label("description");
        licenseDetails.add(description);
        
        PropertyModel<String> linkModel = new PropertyModel<String>(form.getModel(), "link");
        licenseLink = new ExternalLink("link", linkModel, linkModel);
        licenseDetails.add(licenseLink);
        
        downloadLink = new ResourceLink<Void>("fileDownload", (IResource) null);
        downloadLink.setVisible(false);
        Label fileName = new Label("fileName", new PropertyModel<String>(form.getModel(), "attachmentFileName"));
        downloadLink.add(fileName);
        licenseDetails.add(downloadLink);
    }


    @SuppressWarnings("serial")
    private void addControls() {

        // save button
        saveButton = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
            
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onSubmitAction(LicensePriceForm.this.form.getModelObject(), priceInput.getModelObject(), target, form);
                target.add(form);
                clearForm();
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(form);
                clearForm();
            }

        };
        
        saveButton.setVisibilityAllowed(false);
        form.add(saveButton);

        // cancel button
        AjaxButton cancelButton = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
            
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.clearInput();
                onCancelAction(target, form);
                clearForm();
            }
        };
        
        cancelButton.setDefaultFormProcessing(false);
        form.add(cancelButton);
    }

    
    @SuppressWarnings("serial")
    private void addDropDownChoice(final IModel<List<License>> licenseChoices) {
        
        dropDownChoice = new AjaxDropDownChoice<License>("licenseSelect", new Model<License>(), licenseChoices, new ChoiceRenderer<License>("title")) {

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, final License option) {
                if (option == null || option.getLicenseId() == 0) {
                    //LicensePriceForm.this.form.setModelObject(null);
                    //LicensePriceForm.this.form.clearInput();
                    clearForm();
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
                    
                    LicensePriceForm.this.form.setModelObject(option);
                    saveButton.setVisibilityAllowed(true);
                }
                
                target.add(LicensePriceForm.this);
            }
            
        };

        dropDownChoice.setNullValid(true);
        form.add(dropDownChoice);
    }
    
    
    private void clearForm() {
        form.setModelObject(null);
        dropDownChoice.setDefaultModelObject(null);
        priceInput.setModelObject(BigDecimal.ZERO);
        priceInput.setEnabled(false);
        saveButton.setVisibilityAllowed(false);
        licenseDetails.setVisible(false);
    }

}
