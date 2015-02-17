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
 *   ExperimentsDownloadPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.OrderItem;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageFacade;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentDownloadProvider;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.components.ViewLicensePanel;

/**
 * Page for download experiment file.
 * 
 * @author Jakub Rinkes
 * 
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentsDownloadPage extends MenuPage {

    private static final long serialVersionUID = 957980612639804114L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ExperimentsFacade facade;

    @SpringBean
    PersonFacade personFacade;

    @SpringBean
    SecurityFacade security;

    @SpringBean
    ExperimentDownloadProvider downloadProvider;

    @SpringBean
    OrderFacade orderFacade;

    @SpringBean
    ExperimentPackageFacade expPcgFacade;

    @SpringBean
    LicenseFacade licenseFacade;

    public ExperimentsDownloadPage(PageParameters parameters) {

        int experimentId = parseParameters(parameters);

        setPageTitle(ResourceUtils.getModel("pageTitle.chooseMetadata"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        Experiment experiment = facade.getExperimentForDetail(experimentId);

        add(new ExperimentDownloadForm("form", experiment, downloadProvider, getFeedback()));
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

    // inner form for information used in generator.
    private class ExperimentDownloadForm extends Form<MetadataCommand> {

        private static final long serialVersionUID = 1L;

        public ExperimentDownloadForm(String id, final Experiment experiment, final ExperimentDownloadProvider downloadProvider, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<MetadataCommand>(new MetadataCommand()));
            setOutputMarkupId(true);

            boolean canSeePersonInfoAboutUser = false;
            Person loggedPerson = personFacade.getLoggedPerson();
            if (loggedPerson.getAuthority().equals("ROLE_ADMIN")) {
                canSeePersonInfoAboutUser = true;
            }
            if (experiment.getPersonByOwnerId().getPersonId() == loggedPerson.getPersonId()) {
                canSeePersonInfoAboutUser = true;
            }
            if (personFacade.userNameInGroup(loggedPerson.getUsername(), experiment.getResearchGroup().getResearchGroupId())) {
                canSeePersonInfoAboutUser = true;
            }

            // checkbox for group of checkboxes about person
            add(new AjaxCheckBox("person") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    target.add(ExperimentDownloadForm.this);
                }
            });
            // checkbox for group of checkboxes about scenario
            add(new AjaxCheckBox("scenario") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    target.add(ExperimentDownloadForm.this);
                }
            });
            // checkbox for group of checkboxes about measuration
            add(new AjaxCheckBox("measuration") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    target.add(ExperimentDownloadForm.this);
                }
            });

            // personal info is hidded bud if u check all its checked in data too.
            // so before generate file is this unchecked in command manualy
            // all checkboxes in checkbox groups.
            add(new CheckBox("name").setVisibilityAllowed(canSeePersonInfoAboutUser));
            add(new CheckBox("birth").setVisibilityAllowed(canSeePersonInfoAboutUser));
            add(new CheckBox("gender"));
            add(new CheckBox("phoneNumber").setVisibilityAllowed(canSeePersonInfoAboutUser));
            add(new CheckBox("email").setVisibilityAllowed(canSeePersonInfoAboutUser));
            add(new CheckBox("note"));
            add(new CheckBox("personAddParams"));
            add(new CheckBox("title"));
            add(new CheckBox("length"));
            add(new CheckBox("description"));
            add(new CheckBox("scenFile"));
            add(new CheckBox("times"));
            add(new CheckBox("temperature"));
            add(new CheckBox("weather"));
            add(new CheckBox("weatherNote"));
            add(new CheckBox("hardware"));
            add(new CheckBox("measurationAddParams"));

            // there start wicket magic. we create map, in map is for file id list of his parameters. here will be stored checked parameters for file.
            final Map<Integer, Set<FileMetadataParamVal>> paramSelect = new HashMap<Integer, Set<FileMetadataParamVal>>();
            // create check group for checking above collection. there will be stored checked files.
            final CheckGroup<DataFile> group = new CheckGroup<DataFile>("filecheck", new HashSet<DataFile>());

            // chooseAll checkbox indicate via metadatacommand information - all data is checked and ajax update page.
            add(new AjaxCheckBox("chooseAll") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    Boolean checked = getModelObject();
                    // for indicate information about all files is checked we have to add right file object in right sets.
                    if (checked) { // if ve check all we have to check file. File is checked via adding into lists and groups.
                        for (DataFile file : experiment.getDataFiles()) {
                            // for file add parameters in set in map - this check all parameters in list
                            paramSelect.get(file.getDataFileId()).addAll(file.getFileMetadataParamVals());
                            // add file in group - this check file content in list
                            group.getModelObject().add(file);
                        }
                    } else { // else we need uncheck all. so remove datafiles from group, clear lists in map for this files.

                        for (DataFile file : experiment.getDataFiles()) {
                            paramSelect.get(file.getDataFileId()).clear();
                            group.getModelObject().clear();
                        }
                    }

                    target.add(ExperimentDownloadForm.this);
                }
            });

            // create list of file which contains some information and list of parameters. Everything can be checked.
            ListView<DataFile> list = new ListView<DataFile>("fileList", new ArrayList<DataFile>(experiment.getDataFiles())) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(final ListItem<DataFile> item) {
                    // add checkbox for file group. this checkbox check file
                    final DataFile dataFile = item.getModelObject();
                    item.add(new Check<DataFile>("checkbox", item.getModel(), group));
                    item.add(new Label("filename", dataFile.getFilename()));

                    // created list for parameters
                    Set<FileMetadataParamVal> list = new HashSet<FileMetadataParamVal>();
                    // and put it in paramateres map under file id
                    paramSelect.put(item.getModelObject().getDataFileId(), list);
                    // created inner checkgroup for checking above parameter list in map.
                    final CheckGroup<FileMetadataParamVal> valGroup = new CheckGroup<FileMetadataParamVal>("valGroup", list);
                    // created list for parameters with information and checkbox
                    PropertyListView<FileMetadataParamVal> valList = new PropertyListView<FileMetadataParamVal>("params", new ArrayList<FileMetadataParamVal>(dataFile
                            .getFileMetadataParamVals())) {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void populateItem(ListItem<FileMetadataParamVal> itemInner) {
                            // add checkbox for parameter, this checkbox check parameter.
                            itemInner.add(new Check<FileMetadataParamVal>("checkboxVal", itemInner.getModel(), valGroup));
                            itemInner.add(new Label("metadataValue"));
                            itemInner.add(new Label("fileMetadataParamDef.paramName"));
                            itemInner.add(new Label("fileMetadataParamDef.paramDataType"));
                        }
                    };
                    valList.setReuseItems(true);
                    item.add(valGroup.add(valList));

                    /*
                     * Loadable model - used for indication if we check all files.
                     */
                    LoadableDetachableModel<Boolean> checkFullModel = new LoadableDetachableModel<Boolean>() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected Boolean load() {

                            DataFile datafile = item.getModelObject();
                            boolean fileSelected = group.getModelObject().contains(datafile);
                            boolean allValuesSelected = paramSelect.get(datafile.getDataFileId()).containsAll(datafile.getFileMetadataParamVals());

                            if (fileSelected && allValuesSelected)
                                return true;
                            else
                                return false;

                        }
                    };

                    // added check all button for every file in experiment. check file content and all params.
                    item.add(new AjaxCheckBox("checkFull", checkFullModel) {

                        private static final long serialVersionUID = 1L;

                        protected void onUpdate(AjaxRequestTarget target) {

                            Boolean checked = getModelObject();
                            int dataFileId = dataFile.getDataFileId();

                            if (!checked) { // add params to list in map and add file in group.
                                group.getModelObject().remove(dataFile);
                                paramSelect.get(dataFileId).clear();
                            } else { // clear list in map for this file and remove file from group
                                group.getModelObject().add(dataFile);
                                paramSelect.get(dataFileId).addAll(dataFile.getFileMetadataParamVals());
                            }
                            target.add(ExperimentDownloadForm.this);
                        };
                    });

                }
            };
            list.setReuseItems(true);

            group.add(list);
            add(group);

            final boolean uncheckPersonInfo = canSeePersonInfoAboutUser;
            SubmitLink submit = new SubmitLink("submit") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onSubmit() {
                    // because we used object for check we have all data rdy to use.
                    MetadataCommand command = ExperimentDownloadForm.this.getModelObject();
                    if (!uncheckPersonInfo) {
                        command.setName(false);
                        command.setBirth(false);
                        command.setEmail(false);
                        command.setPhoneNumber(false);
                    }

                    Collection<DataFile> files = group.getModelObject();
                    FileDTO outputFile = downloadProvider.generate(experiment, command, files, paramSelect);

                    if (outputFile == null || outputFile.getFile() == null)
                        error("Error while file is generated. Can't be downloaded.");
                    else {
                        getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
                    }
                }
            };

            add(submit);

            final ModalWindow viewLicenseWindow = new ModalWindow("viewLicenseWindow");
            viewLicenseWindow.setAutoSize(true);
            viewLicenseWindow.setResizable(false);
            viewLicenseWindow.setMinimalWidth(700);
            viewLicenseWindow.setWidthUnit("px");
            add(viewLicenseWindow);

            final IModel<License> licenseModel = new Model<License>();
            licenseModel.setObject(licenseFacade.getPublicLicense());

            viewLicenseWindow.setContent(new ViewLicensePanel(viewLicenseWindow.getContentId(), licenseModel));
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
    }

}
