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
 *   ExperimentsPackageDownloadPage.java, 2014/11/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.progressbar.ProgressBar;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.DownloadPackageManager;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentDownloadProvider;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.ViewLicensePanel;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentsPackageDownloadPage extends MenuPage {

    private static final long serialVersionUID = -2458549683564604189L;

    @SpringBean
    private ExperimentPackageFacade expPckFacade;
    
    @SpringBean
    private ExperimentsFacade expFacade;

    @SpringBean
    private ExperimentDownloadProvider downloadProvider;

    @SpringBean
    private PersonFacade personFacade;

    @SpringBean
    private LicenseFacade licenseFacade;

    public ExperimentsPackageDownloadPage(PageParameters parameters) {

        StringValue packageIdValue = parameters.get(DEFAULT_PARAM_ID);
        if (packageIdValue.isNull() || packageIdValue.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListExperimentsByPackagePage.class);
        else
            setupComponents(packageIdValue.toInt());
    }

    private void setupComponents(int packageId) {

        setPageTitle(ResourceUtils.getModel("pageTitle.downloadPackage"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        ExperimentPackage experimentPackage = expPckFacade.read(packageId);

        add(new Label("packageName", experimentPackage.getName()));
        add(new ExperimentPackageDownloadForm("form", experimentPackage, downloadProvider, expPckFacade));

    }

    private class ExperimentPackageDownloadForm extends Form<MetadataCommand> implements IAjaxIndicatorAware{

        private static final long serialVersionUID = 163130449536922222L;
        private IModel<License> licenseModel;
        private ProgressBar progressBar;
        AjaxSubmitLink submit;
        private int step = 0;
        Link<Void> downloadLink;
        FileDTO outputFile;
        DownloadPackageManager manager;
        private final AjaxIndicatorAppender indicator = new AjaxIndicatorAppender();

        public ExperimentPackageDownloadForm(String id, final ExperimentPackage expPackage, final ExperimentDownloadProvider downloadProvider,
                final ExperimentPackageFacade expPckFacade) {
            super(id, new CompoundPropertyModel<MetadataCommand>(new MetadataCommand()));
            setOutputMarkupId(true);

            final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
            add(feedback.setOutputMarkupId(true));

            boolean canSeePersonInfoAboutUser = false;
            Person loggedPerson = EEGDataBaseSession.get().getLoggedUser();
            if (loggedPerson.getAuthority().equals("ROLE_ADMIN")) {
                canSeePersonInfoAboutUser = true;
            }

            if (personFacade.userNameInGroup(loggedPerson.getUsername(), expPackage.getResearchGroup().getResearchGroupId())) {
                canSeePersonInfoAboutUser = true;
            }
            
            List<Experiment> experiments = expFacade.getExperimentsByPackage(expPackage.getExperimentPackageId());
            final List<Experiment> selectList = new ArrayList<Experiment>(experiments);

            final CheckGroup<Experiment> group = new CheckGroup<Experiment>("group", selectList);

            final AbstractAjaxTimerBehavior timer = new AbstractAjaxTimerBehavior(Duration.ONE_SECOND) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onTimer(AjaxRequestTarget target)
                {
                    if (manager != null) {
                        if (manager.getNumberOfExperiments() == 0) {
                            step = 100;
                        } else {
                            double percentage = (double) manager.getNumberOfDownloadedExperiments() / manager.getNumberOfExperiments() * 100;
                            System.out.println(manager.getNumberOfDownloadedExperiments());
                            System.out.println(percentage);
                            step = (int) percentage - progressBar.getModelObject();
                        }
                    }
                    progressBar.forward(target, step);
                }
            };

            add(timer);

            progressBar = new ProgressBar("progress", Model.of(0)) {

                private static final long serialVersionUID = 1L;

                @Override
                public void onValueChanged(AjaxRequestTarget target)
                {
                    //this.getDefaultModelObjectAsString()
                    info("Creating package: " + manager.getNumberOfDownloadedExperiments() + "/" + manager.getNumberOfExperiments());
                    target.add(feedback);
                }

                @Override
                protected void onComplete(AjaxRequestTarget target)
                {
                    timer.stop(target); //wicket6

                    info("Package created!");
                    if (manager.isAlive()) {
                        try {
                            manager.join();
                        } catch (InterruptedException e) {
                            error("Error");
                        }
                    }
                    outputFile = manager.getOutputFile();
                    downloadLink.setVisible(true);
                    target.add(downloadLink);
                    target.add(downloadLink);
                }
            };


            add(this.progressBar);


            // Indicator //
            //add(new EmptyPanel("indicator").add(this.indicator));

            // Initialize FeedbackPanel //
            //this.info("value: " + this.progressBar.getDefaultModelObjectAsString());

            downloadLink = new Link<Void>("downloadLink") {
                @Override
                public void onClick() {

                    if (outputFile == null || outputFile.getFile() == null)
                        error("Error while file is generated. Can't be downloaded.");
                    else if (outputFile.isDeleted()) {

                        setResponsePage(ExperimentsPackageDownloadPage.class, PageParametersUtils.getDefaultPageParameters(expPackage.getExperimentPackageId()));
                    }
                    else {
                        outputFile.setDeleted(true);
                        getPage().getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
                    }


                }
            };
            downloadLink.setOutputMarkupPlaceholderTag(true);
            downloadLink.setOutputMarkupId(true);
            downloadLink.setVisible(false);
            add(downloadLink);

            group.add(new CheckGroupSelector("selectedAll", group));

            PageableListView<Experiment> experimentsList = new PageableListView<Experiment>("selectExp", experiments, Integer.MAX_VALUE) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Experiment> item) {
                    item.setModel(new CompoundPropertyModel<Experiment>(item.getModel()));
                    item.add(new Check<Experiment>("selected", item.getModel()));
                    item.add(new Label("experimentId"));
                    item.add(new Label("scenario.title"));
                    item.add(new Label("startTime"));
                    item.add(new Label("personBySubjectPersonId.gender"));
                    item.add(new Label("personBySubjectPersonId.dateOfBirth"));
                }
            };

            group.add(experimentsList);
            group.setOutputMarkupPlaceholderTag(true);
            group.setOutputMarkupId(true);
            group.setRenderBodyOnly(false);
           // add(new AjaxPagingNavigator("navigator",experimentsList));
            add(group);

            // checkbox for group of checkboxes about person
//            add(new AjaxCheckBox("person") {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                protected void onUpdate(AjaxRequestTarget target) {
//
//                    target.add(ExperimentPackageDownloadForm.this);
//                }
//            });
//            // checkbox for group of checkboxes about scenario
//            add(new AjaxCheckBox("scenario") {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                protected void onUpdate(AjaxRequestTarget target) {
//
//                    target.add(ExperimentPackageDownloadForm.this);
//                }
//            });
//            // checkbox for group of checkboxes about measuration
//            add(new AjaxCheckBox("measuration") {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                protected void onUpdate(AjaxRequestTarget target) {
//
//                    target.add(ExperimentPackageDownloadForm.this);
//                }
//            });
//
//            // personal info is hidded bud if u check all its checked in data too.
//            // so before generate file is this unchecked in command manualy
//            // all checkboxes in checkbox groups.
//
//            add(new CheckBox("name").setVisibilityAllowed(canSeePersonInfoAboutUser));
//            add(new CheckBox("birth").setVisibilityAllowed(canSeePersonInfoAboutUser));
//            add(new CheckBox("gender"));
//            add(new CheckBox("phoneNumber").setVisibilityAllowed(canSeePersonInfoAboutUser));
//            add(new CheckBox("email").setVisibilityAllowed(canSeePersonInfoAboutUser));
//            add(new CheckBox("note"));
//            add(new CheckBox("personAddParams"));
//            add(new CheckBox("title"));
//            add(new CheckBox("length"));
//            add(new CheckBox("description"));
//            add(new CheckBox("scenFile"));
//            add(new CheckBox("times"));
//            add(new CheckBox("temperature"));
//            add(new CheckBox("weather"));
//            add(new CheckBox("weatherNote"));
//            add(new CheckBox("hardware"));
//            add(new CheckBox("measurationAddParams"));
//
//            // chooseAll checkbox indicate via metadatacommand information - all data is checked and ajax update page.
//            add(new AjaxCheckBox("chooseAll") {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                protected void onUpdate(AjaxRequestTarget target) {
//                    target.add(ExperimentPackageDownloadForm.this);
//                }
//            });

            final boolean uncheckPersonInfo = canSeePersonInfoAboutUser;
            submit = new AjaxSubmitLink("submit", this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    // because we used object for check we have all data rdy to use.
                    //progressBar.setVisible(true);
                    this.setVisible(false);
                    group.setVisible(false);
                    target.add(group);
                    target.add(this);

                    MetadataCommand command = ExperimentPackageDownloadForm.this.getModelObject();
                    if (!uncheckPersonInfo) {
                        command.setName(false);
                        command.setBirth(false);
                        command.setEmail(false);
                        command.setPhoneNumber(false);
                    }
                    manager = new DownloadPackageManager(expPackage, command, licenseModel.getObject(), selectList, downloadProvider, personFacade.getLoggedPerson());
                    manager.start();
                    timer.restart(target);
                    progressBar.setModelObject(0);

                    //outputFile = downloadProvider.generatePackageFile(expPackage, command, licenseModel.getObject(), selectList);

//                    if (outputFile == null || outputFile.getFile() == null)
//                        error("Error while file is generated. Can't be downloaded.");
//                    else {
//                        getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
//                    }
                }
            };
            submit.setOutputMarkupId(true);
            submit.setOutputMarkupPlaceholderTag(true);
            add(submit);
            
            final ModalWindow viewLicenseWindow = new ModalWindow("viewLicenseWindow");
            viewLicenseWindow.setAutoSize(true);
            viewLicenseWindow.setResizable(false);
            viewLicenseWindow.setMinimalWidth(700);
            viewLicenseWindow.setWidthUnit("px");
            add(viewLicenseWindow);
            
            licenseModel = new Model<License>();
            License license = licenseFacade.getLicenseForPurchasedExpPackage(expPackage.getExperimentPackageId(), EEGDataBaseSession.get().getLoggedUser().getPersonId());
            licenseModel.setObject(license);
                
            viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel, false));
            viewLicenseWindow.setTitle(ResourceUtils.getModel("dataTable.heading.licenseTitle"));
            AjaxLink<License> viewLicenseLink = new AjaxLink<License>("viewLicenseLink", licenseModel) {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    viewLicenseWindow.show(target);
                }

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    this.setVisible(licenseModel.getObject() != null);
                }

            };
            viewLicenseLink.setOutputMarkupPlaceholderTag(true);
            add(viewLicenseLink);

        }

        @Override
        public String getAjaxIndicatorMarkupId() {
            return null;
        }
    }
}
