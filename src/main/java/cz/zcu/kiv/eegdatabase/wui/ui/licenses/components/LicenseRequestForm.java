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
 *   LicenseRequestForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.hibernate.Hibernate;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;

/**
 * Form for license request.
 * 
 * @author Jakub Danek
 */
public class LicenseRequestForm extends Panel {

    private static final long serialVersionUID = -5784676290092286346L;
    
    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private LicenseFacade licenseFacade;

    private IModel<License> licenseModel;
    private PersonalLicense persLicense;
    private boolean displayControls = true;

    private FileUploadField uploadField;

    private Form form;

    public LicenseRequestForm(String id, IModel<License> license) {
        super(id);
        this.licenseModel = license;
        form = new StatelessForm("form");
        this.persLicense = new PersonalLicense();
        this.add(form);
        this.add(new Label("title", ResourceUtils.getModel("heading.license.request", licenseModel)));

        this.addComponents();
        this.addControls();
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        // ugly hack to disable stupid global feedback
        ((MenuPage) this.getPage()).getFeedback().setEnabled(false);
    }

    private void addComponents() {
        FormComponent c = new RequiredTextField("firstName", new PropertyModel(persLicense, "firstName"));
        c.setLabel(ResourceUtils.getModel("label.license.request.firstname"));
        form.add(c);

        c = new RequiredTextField("lastName", new PropertyModel(persLicense, "lastName"));
        c.setLabel(ResourceUtils.getModel("label.license.request.lastname"));
        form.add(c);

        c = new RequiredTextField("organisation", new PropertyModel(persLicense, "organisation"));
        c.setLabel(ResourceUtils.getModel("label.license.request.organisation"));
        form.add(c);

        c = new RequiredTextField("email", new PropertyModel(persLicense, "email"));
        c.add(EmailAddressValidator.getInstance());
        c.setLabel(ResourceUtils.getModel("label.license.request.email"));
        form.add(c);

        uploadField = new FileUploadField("attachement");
        uploadField.setLabel(ResourceUtils.getModel("label.license.request.attachement"));
        form.add(uploadField);
        form.setMaxSize(Bytes.megabytes(15));

        WicketUtils.addLabelsAndFeedback(form);
    }

    private void addControls() {
        Button b = new Button("submitButton", ResourceUtils.getModel("button.save")) {

            @Override
            public void onSubmit() {
                super.onSubmit();
                LicenseRequestForm.this.onSubmitAction();
            }

        };
        form.add(b);

        b = new Button("cancelButton", ResourceUtils.getModel("button.cancel")) {

            @Override
            public void onSubmit() {
                super.onSubmit();
                form.clearInput();
                LicenseRequestForm.this.onCancelAction();
            }

        };
        b.setDefaultFormProcessing(false);
        form.add(b);
    }

    /**
     * Sets license and person for the submited PersonalLicense object and creates a request for the license. Override to provide additional actions.
     */
    protected void onSubmitAction() {
        FileUpload f = uploadField.getFileUpload();
        if (f != null) {
            persLicense.setAttachmentFileName(f.getClientFileName());
            try {
                licenseModel.getObject().setFileContentStream(f.getInputStream());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        persLicense.setLicense(licenseModel.getObject());
        persLicense.setPerson(EEGDataBaseSession.get().getLoggedUser());
        licenseFacade.createRequestForLicense(persLicense);
        
        licenseModel.getObject().setFileContentStream(null);
    }

    protected void onCancelAction() {
        // override this
    }

    public boolean isDisplayControls() {
        return displayControls;
    }

    public void setDisplayControls(boolean displayControls) {
        this.displayControls = displayControls;
    }

}
