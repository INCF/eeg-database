package cz.zcu.kiv.eegdatabase.wui.ui.administration.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.MembershipPlanType;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdminManageMembershipPlansPage;
import cz.zcu.kiv.eegdatabase.wui.ui.administration.AdministrationPageLeftMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Lichous on 4.5.15.
 */

@AuthorizeInstantiation(value = {"ROLE_ADMIN" })
public class LicenseManageFormPage extends MenuPage {
    private static final long serialVersionUID = -1642766215588431080L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    LicenseFacade licenseFacade;

    public LicenseManageFormPage() {
        add(new Label("headTitle", ResourceUtils.getModel("pageTitle.addLicenseTemplate")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new LicenseForm("form",new Model<License>(new License()),licenseFacade,getFeedback()));

    }

    public LicenseManageFormPage(PageParameters parameters) {
        StringValue licenseId = parameters.get(DEFAULT_PARAM_ID);
        if (licenseId.isNull() || licenseId.isEmpty())
            throw new RestartResponseAtInterceptPageException(AdminManageLicensesPage.class);

        License license = licenseFacade.read(licenseId.toInteger());

        add(new Label("headTitle", ResourceUtils.getModel("pageTitle.editLicenseTemplate")));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));
        add(new LicenseForm("form",new Model<License>(license),licenseFacade,getFeedback()));

    }

    private class LicenseForm extends Form<License> {

        public LicenseForm(String id, IModel<License> model,final LicenseFacade licenseFacade, final FeedbackPanel feedback) {
            super(id,new CompoundPropertyModel<License>(model));

            TextField<String> name = new TextField<String>("title");
            name.setLabel(ResourceUtils.getModel("label.name"));
            name.setRequired(true);
            name.add(StringValidator.maximumLength(255));
            name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
            FormComponentLabel nameLabel = new FormComponentLabel("titleLb", name);
            add(name, nameLabel);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.description"));
            description.add(StringValidator.maximumLength(255));
            FormComponentLabel descriptionLabel = new FormComponentLabel("descriptionLb", description);
            add(description,descriptionLabel);

            RadioGroup<LicenseType> type = new RadioGroup<LicenseType>("licenseType", new PropertyModel<LicenseType>(model, "licenseType"));
            type.setLabel(ResourceUtils.getModel("label.license.type"));
            type.setRequired(true);
            type.add(new Radio("public", new Model(LicenseType.OPEN_DOMAIN)));
            type.add(new Radio("academic", new Model(LicenseType.ACADEMIC)));
            type.add(new Radio("business", new Model(LicenseType.BUSINESS)));

            FormComponentLabel typeLabel = new FormComponentLabel("typeLb", type);
            add(type,typeLabel);
            
            TextField<String> link = new TextField<String>("link");
            link.setLabel(ResourceUtils.getModel("label.link"));
            link.add(StringValidator.maximumLength(255));
            link.add(new UrlValidator());
            FormComponentLabel linkLabel = new FormComponentLabel("linkLb", link);
            add(link, linkLabel);

            final FileUploadField fileUpload = new FileUploadField("attachmentFileName");
            FormComponentLabel fileLabel = new FormComponentLabel("attachmentFileNameLb",fileUpload);

            setMaxSize(Bytes.megabytes(15));
            add(fileUpload,fileLabel);

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.saveLicense"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    License license = LicenseForm.this.getModelObject();
                    
                    // TODO kuba licence
                    //if (license.getPrice()==null) license.setPrice(new BigDecimal(0));
                    
                    FileUpload uploadedFile = fileUpload.getFileUpload();

                    if (uploadedFile != null) {
                        license.setAttachmentFileName(uploadedFile.getClientFileName());
                        try {
                            license.setFileContentStream(uploadedFile.getInputStream());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }

                    if (license.getLicenseId() == 0) {
                        license.setTemplate(true);
                        licenseFacade.create(license);

                    } else {
                        if (uploadedFile == null) {
                            license.setAttachmentFileName(licenseFacade.read(license.getLicenseId()).getAttachmentFileName());
                        }
                        licenseFacade.update(license);
                    }

                    license.setFileContentStream(null);

                    setResponsePage(AdminManageLicensesPage.class);

                    target.add(feedback);

                }
            };
            add(submit);

        }
    }

}
