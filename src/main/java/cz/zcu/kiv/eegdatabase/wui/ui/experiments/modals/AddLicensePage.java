package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.WicketUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentLicense.ExperimentLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.*;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichous on 16.5.15.
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class AddLicensePage extends WebPage {
    
    private static final long serialVersionUID = 6965423677772732931L;

    public List<License> licenses;

    
    public AddLicensePage(final PageReference modalWindowPage, final ModalWindow window, final IModel<Experiment> model) {
        licenses = new ArrayList<License>();
        add(new LicenseForm("addForm", window, model));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    public List<License> getLicenses() {
        return licenses;
    }

    protected void onSubmitAction(IModel<License> model, Integer id, AjaxRequestTarget target, Form<?> form) {
    }

    
    private class LicenseForm extends Form<License> {

        @SpringBean
        LicenseFacade licenseFacade;

        @SpringBean
        ExperimentLicenseFacade experimentLicenseFacade;

        private ModalWindow window;
        protected IModel<License> licenseModel;
        
        private Form<License> form;
        private AjaxDropDownChoice<License> ddc;
        private NumberTextField<BigDecimal> priceInput;
        private AjaxButton saveButton;
        
        private WebMarkupContainer licenseDetails;
        private ExternalLink licenseLink;
        private ResourceLink<Void> downloadLink;
        

        public LicenseForm(String id, final ModalWindow window, final IModel<Experiment> model) {
            super(id, new CompoundPropertyModel<License>(new License()));
            form = this;
            this.window=window;
            licenseModel = new Model<License>();
            addFormFields();
            addControls();
            addBlueprintSelect();
            addLicenseDetails();
        }

        private void addFormFields() {
            //priceInput = new NumberTextField<BigDecimal>("price", new PropertyModel<BigDecimal>(licenseModel, "price"), BigDecimal.class);
            priceInput = new NumberTextField<BigDecimal>("price");
            priceInput.setModel(new Model<BigDecimal>(BigDecimal.ZERO));
            priceInput.setMinimum(BigDecimal.ZERO);
            priceInput.setRequired(true);
            priceInput.setEnabled(false);
            priceInput.setLabel(ResourceUtils.getModel("label.license.price"));
            add(priceInput);

            WicketUtils.addLabelsAndFeedback(this);
        }
        
        
        private void addLicenseDetails() {
            licenseDetails = new WebMarkupContainer("licenseDetails");
            licenseDetails.setVisible(licenseModel.getObject() != null);
            add(licenseDetails);
            
            Label title = new Label("title", new PropertyModel<String>(licenseModel, "title"));
            licenseDetails.add(title);
            
            Label type = new Label("type", new PropertyModel<String>(licenseModel, "licenseType"));
            licenseDetails.add(type);
            
            Label description = new Label("description", new PropertyModel<String>(licenseModel, "description"));
            licenseDetails.add(description);
            
            PropertyModel<String> linkModel = new PropertyModel<String>(licenseModel, "link");
            licenseLink = new ExternalLink("link", linkModel, linkModel);
            licenseLink.setVisible(linkModel.getObject() != null);
            licenseDetails.add(licenseLink);
            
            ByteArrayResource res;
            res = new ByteArrayResource("") {
                @Override
                public void configureResponse(final AbstractResource.ResourceResponse response, final IResource.Attributes attributes) {
                    response.setCacheDuration(Duration.NONE);
                }
            };
            downloadLink = new ResourceLink<Void>("fileDownload", res);
            downloadLink.setVisible(false);
            licenseDetails.add(downloadLink);
            
            Label l = new Label("fileName", new PropertyModel<String>(licenseModel, "attachmentFileName"));
            downloadLink.add(l);
        }


        private void addControls() {
            saveButton = new AjaxButton("submitButton", ResourceUtils.getModel("button.save")) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onSubmitAction(licenseModel, ddc.getModel().getObject().getLicenseId(), target, form);
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
            saveButton.setVisibilityAllowed(false);
            add(saveButton);

            AjaxButton button = new AjaxButton("cancelButton", ResourceUtils.getModel("button.cancel")) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    form.clearInput();
                    window.close(target);
                }

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    this.setVisible(true);
                }
            };
            button.setDefaultFormProcessing(false);
            add(button);

        }

        private void addBlueprintSelect() {
            IModel<License> selectedBlueprintModel = new Model<License>();
            final IModel<List<License>> blueprintsModel = new LoadableDetachableModel<List<License>>() {
                @Override
                protected List<License> load() {
                    return licenseFacade.getLicenseTemplates();
                }
            };
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
                        licenseDetails.setVisible(false);
                        downloadLink.setVisible(false);
                    } else {
                        
                        // TODO kuba licence
                        priceInput.setEnabled(option.getLicenseType() == LicenseType.COMMERCIAL);
                        
                        License l = new License();
                        l.setTitle(option.getTitle());
                        l.setDescription(option.getDescription());
                        l.setLicenseType(option.getLicenseType());
                        l.setLink(option.getLink());
                        //l.setPrice(option.getPrice());
                        l.setAttachmentFileName(option.getAttachmentFileName());
                        l.setTemplate(false);
                        l.setFileContentStream(null);
                        
                        licenseDetails.setVisible(true);
                        licenseLink.setVisible(l.getLink() != null);

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
                        
                        licenseModel.setObject(l);
                        saveButton.setVisibilityAllowed(true);
                    }
                    target.add(form);
                }
            };

            ddc.setNullValid(true);


            add(ddc);
        }
    }
}

