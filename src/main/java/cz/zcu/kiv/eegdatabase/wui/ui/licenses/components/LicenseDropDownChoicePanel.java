package cz.zcu.kiv.eegdatabase.wui.ui.licenses.components;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentLicense.ExperimentLicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.membershipplan.PersonMembershipPlanFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ManageExperimentsPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lichous on 26.4.15.
 */
public class LicenseDropDownChoicePanel extends Panel {

    private static final long serialVersionUID = 1875827209151471736L;

    protected Log log = LogFactory.getLog(getClass());

    private IModel<License> licenseModel;
    private IModel<List<License>> licenses;
    private IModel<Experiment> experimentIModel;
    private ModalWindow viewLicenseWindow;
    private ModalWindow addLicenseWindow;
    private AjaxLink<License> viewLicenseLink;
    private AjaxLink<License> addLicenseLink;
    private AjaxLink<License> removeLicenseLink;
    private Person loggedUser;
    private Class<? extends MenuPage> page;
    private boolean hasActiveMembershipPlan;
    private boolean showRemoveButton;
    private LicenseEditForm licenseEditForm;
    private AjaxDropDownChoice<License> licensesChoice;

    @SpringBean
    private LicenseFacade licenseFacade;

    @SpringBean
    private ExperimentLicenseFacade experimentLicenseFacade;

    @SpringBean
    private PersonMembershipPlanFacade personMembershipPlanFacade;


    public LicenseDropDownChoicePanel (String id, final IModel<Experiment> model, Class<? extends MenuPage> page, boolean showRemoveButton) {
        super(id);
        this.licenseModel = new Model();
        this.experimentIModel = model;
        this.page = page;
        this.loggedUser = EEGDataBaseSession.get().getLoggedUser();
        this.hasActiveMembershipPlan = personMembershipPlanFacade.hasActiveMembershipPlan(loggedUser);
        this.showRemoveButton = showRemoveButton;
        licenses = new LoadableDetachableModel<List<License>>() {

            @Override
            protected List<License> load() {
                List<License> l = licenseFacade.getLicensesForExperiment(model.getObject().getExperimentId());
                return l;
            }
        };

        licensesChoice = new AjaxDropDownChoice<License>("licenses",licenseModel,licenses, new ChoiceRenderer<License>("title", "licenseId")){

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, License option) {
                super.onSelectionChangeAjaxified(target, option);

                target.add(viewLicenseLink);
                target.add(removeLicenseLink);

            }

        };

        licensesChoice.setVisibilityAllowed(hasActiveMembershipPlan);
        licensesChoice.setNullValid(true);
        add(licensesChoice);
        this.addLicenseViewWindow();
        this.addLicenseAddWindow();

    }

    private void addLicenseAddWindow() {
        addLicenseWindow = new ModalWindow("addLicenseWindow");
        addLicenseWindow.setAutoSize(true);
        addLicenseWindow.setResizable(false);
        addLicenseWindow.setMinimalWidth(600);
        addLicenseWindow.setWidthUnit("px");
        addLicenseWindow.showUnloadConfirmation(false);
        addLicenseWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {
                licenseEditForm.clearLicenseModel();
                setResponsePage(ManageExperimentsPage.class);
            }
        });

        IModel<List<License>> blpModel = new LoadableDetachableModel<List<License>>() {
            @Override
            protected List<License> load() {
                return licenseFacade.getLicenseTemplates();
            }
        };

        licenseEditForm = new LicenseEditForm(addLicenseWindow.getContentId(), licenseModel, blpModel, new Model<Boolean>(true)) {

            @Override
            protected void onSubmitAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
                License obj = model.getObject();

                System.out.println("LICENCE: "+obj+"id: "+obj.getLicenseId());

                if (obj.getLicenseId() == 0) {
                    //if(selectedBlueprintModel.getObject() != null && !obj.getTitle().equals(selectedBlueprintModel.getObject().getTitle())) {
                        obj.setTemplate(false);
                        //obj.setResearchGroup(epModel.getObject().getResearchGroup());
                        licenseFacade.create(obj);
                        System.out.println("PO CREATEU: "+obj.getLicenseId());
                    }
                    //licenseFacade.addLicenseForPackage(model.getObject(), epModel.getObject());
                //}
                else {
                    licenseFacade.update(obj);
                }

                System.out.println("EXP ID: "+experimentIModel.getObject().getExperimentId()+", LIC ID: "+obj.getLicenseId());

                ExperimentLicence expLic = new ExperimentLicence();
                expLic.setExperiment(experimentIModel.getObject());
                expLic.setLicense(obj);
                experimentLicenseFacade.create(expLic);

                obj.setFileContentStream(null);
                ModalWindow.closeCurrent(target);
                setResponsePage(page);

            }

            @Override
            protected void onCancelAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
                ModalWindow.closeCurrent(target);
            }

            @Override
            protected void onRemoveAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {
                //licenseFacade.removeLicenseFromPackage(model.getObject(), epModel.getObject());
                ModalWindow.closeCurrent(target);
            }

        };
        licenseEditForm.setDisplayRemoveButton(false);
        addLicenseWindow.setContent(licenseEditForm);
        add(addLicenseWindow);

        addLicenseLink = new AjaxLink<License>("addLicenseLink", licenseModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                //License l = this.getModelObject();
                //if (l!=null) System.out.println(l.getTitle());
                licenseModel.setObject(new License());
                licenseEditForm.setLicenseModel(new License());
                addLicenseWindow.show(target);
            }

        };
        addLicenseLink.setOutputMarkupPlaceholderTag(true);
        addLicenseLink.setVisibilityAllowed(hasActiveMembershipPlan);
        add(addLicenseLink);
    }

    private void addLicenseViewWindow() {
        viewLicenseWindow = new ModalWindow("viewLicenseWindow");
        viewLicenseWindow.setAutoSize(true);
        viewLicenseWindow.setResizable(false);
        viewLicenseWindow.setMinimalWidth(600);
        viewLicenseWindow.setWidthUnit("px");
        viewLicenseWindow.showUnloadConfirmation(false);
        add(viewLicenseWindow);

        viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel,showRemoveButton) {
            @Override
            protected void onRemoveAction(IModel<License> model, AjaxRequestTarget target, Form<?> form) {

                experimentLicenseFacade.remove(experimentIModel.getObject(),model.getObject());
                /*ModalWindow.closeCurrent(target);
                //target.add(header);*/
                setResponsePage(page);
            }
        });
        viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
        viewLicenseLink = new AjaxLink<License>("viewLicenseLink", licenseModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                License l = this.getModelObject();
                viewLicenseWindow.show(target);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(this.getModelObject()!=null);
            }

        };
        viewLicenseLink.setOutputMarkupPlaceholderTag(true);
        add(viewLicenseLink);

        removeLicenseLink = new AjaxLink<License>("removeLicenseLink", licenseModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println(this.getModelObject());
                System.out.println(experimentIModel.getObject());
                //viewLicenseWindow.show(target);
                experimentLicenseFacade.remove(experimentIModel.getObject(),this.getModelObject());
                setResponsePage(page);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(this.getModelObject()!=null);
            }

        };
        removeLicenseLink.setOutputMarkupPlaceholderTag(true);
        add(removeLicenseLink);
    }
}
