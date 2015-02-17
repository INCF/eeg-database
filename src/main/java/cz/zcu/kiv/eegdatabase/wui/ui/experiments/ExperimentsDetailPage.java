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
 *   ExperimentsDetailPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.ArrayList;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampLabel;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.data.AddDataFilePage;
import cz.zcu.kiv.eegdatabase.wui.ui.data.DataFileDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.components.ExperimentBuyDownloadLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;

/**
 * Page of detail on experiment. 
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentsDetailPage extends MenuPage {

    private static final long serialVersionUID = 957980612639804114L;

    @SpringBean
    ExperimentsFacade facade;
    
    @SpringBean
    FileFacade fileFacade;

    @SpringBean
    SecurityFacade security;

    public ExperimentsDetailPage(PageParameters parameters) {

        int experimentId = parseParameters(parameters);

        setupComponents(experimentId);
    }

    private void setupComponents(final int experimentId) {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));

        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        final Experiment experiment = facade.getExperimentForDetail(experimentId);

        add(new Label("experimentId", experiment.getExperimentId()+""));
        add(new TimestampLabel("startTime", experiment.getStartTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new TimestampLabel("endTime", experiment.getEndTime(), StringUtils.DATE_TIME_FORMAT_PATTER));
        add(new Label("temperature", experiment.getTemperature()+""));
        add(new Label("weather.title", experiment.getWeather().getTitle()));
        add(new Label("environmentNote", experiment.getEnvironmentNote()));
        add(new Label("privateExperiment", experiment.isPrivateExperiment()+""));
        add(new Label("scenario.title", experiment.getScenario().getTitle()));
        add(new Label("price", experiment.getPrice()));

        Person personBySubjectPersonId = experiment.getPersonBySubjectPersonId();
        add(new TimestampLabel("dateOfBirth", personBySubjectPersonId.getDateOfBirth(), StringUtils.DATE_TIME_FORMAT_PATTER_ONLY_YEAR));
        add(new EnumLabel<Gender>("gender", Gender.getGenderByShortcut(personBySubjectPersonId.getGender())));
        
        BookmarkablePageLink<Void> personLink = new BookmarkablePageLink<Void>("personLink", PersonDetailPage.class, PageParametersUtils.getDefaultPageParameters(personBySubjectPersonId.getPersonId()));
        personLink.setVisibilityAllowed(security.userCanViewPersonDetails(personBySubjectPersonId.getPersonId()));
        add(personLink);
        BookmarkablePageLink<Void> scenarioLink = new BookmarkablePageLink<Void>("scenarioLink", ScenarioDetailPage.class, PageParametersUtils.getDefaultPageParameters(experiment.getScenario().getScenarioId()));
        add(scenarioLink);
        
        // TODO action box pages
        boolean coexperiment = security.userIsOwnerOrCoexperimenter(experimentId);
        BookmarkablePageLink<Void> addParameterLink = new BookmarkablePageLink<Void>("addParameterLink", ExperimentOptParamValueFormPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        BookmarkablePageLink<Void> addFileLink = new BookmarkablePageLink<Void>("addFileLink", AddDataFilePage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        BookmarkablePageLink<Void> editExpLink = new BookmarkablePageLink<Void>("editExpLink", ExperimentFormPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
        ExperimentBuyDownloadLinkPanel downloadExpLink = new ExperimentBuyDownloadLinkPanel("downloadExpLink", new Model<Experiment>(experiment));
        downloadExpLink.setVisibilityAllowed(experiment.getExperimentPackageConnections().isEmpty());
        add(addParameterLink.setVisibilityAllowed(coexperiment), addFileLink.setVisibilityAllowed(coexperiment), editExpLink.setVisibilityAllowed(coexperiment), downloadExpLink);
        
        /* XXX #66 Java Heap Space Exception : working with big data file in memory.
            final ExperimentSignalViewCanvasPanel experimentViewPanel = new ExperimentSignalViewCanvasPanel("view", experiment);
         */

        PropertyListView<Hardware> hardware = new PropertyListView<Hardware>("hardware", new ListModel<Hardware>(new ArrayList<Hardware>(experiment.getHardwares()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Hardware> item) {
                item.add(new Label("title"));
                item.add(new Label("type"));

            }
        };
        PropertyListView<ExperimentOptParamVal> addParameters = new PropertyListView<ExperimentOptParamVal>("addParameters", new ListModel<ExperimentOptParamVal>(new ArrayList<ExperimentOptParamVal>(
                experiment.getExperimentOptParamVals()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ExperimentOptParamVal> item) {
                item.add(new Label("experimentOptParamDef.paramName"));
                item.add(new Label("paramValue"));

            }
        };
        
        PropertyListView<Software> software = new PropertyListView<Software>("software", new ListModel<Software>(new ArrayList<Software>(
                experiment.getSoftwares()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Software> item) {
                item.add(new Label("title"));
                item.add(new Label("description"));

            }
        };
        
        PropertyListView<Disease> diseases = new PropertyListView<Disease>("diseases", new ListModel<Disease>(new ArrayList<Disease>(
                experiment.getDiseases()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Disease> item) {
                item.add(new Label("title"));
                item.add(new Label("description"));

            }
        };
        
        PropertyListView<Pharmaceutical> pharmaceuticals = new PropertyListView<Pharmaceutical>("pharmaceuticals", new ListModel<Pharmaceutical>(new ArrayList<Pharmaceutical>(
                experiment.getPharmaceuticals()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Pharmaceutical> item) {
                item.add(new Label("title"));
                item.add(new Label("description"));

            }
        };
        
        PropertyListView<ProjectType> projectTypes = new PropertyListView<ProjectType>("projectTypes", new ListModel<ProjectType>(new ArrayList<ProjectType>(
                experiment.getProjectTypes()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ProjectType> item) {
                item.add(new Label("title"));
                item.add(new Label("description"));

            }
        };

        PropertyListView<DataFile> files = new PropertyListView<DataFile>("files", new ListModel<DataFile>(new ArrayList<DataFile>(experiment.getDataFiles()))) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<DataFile> item) {
                
                item.add(new Label("filename"));
                item.add(new Label("description"));
                item.add(new ViewLinkPanel("detail", DataFileDetailPage.class, "dataFileId", item.getModel(), ResourceUtils.getModel("link.detail")));
                item.add(new AjaxLink<Void>("deleteLink") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        fileFacade.delete(item.getModelObject());

                        setResponsePage(ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(experimentId));
                    }

                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                        super.updateAjaxAttributes(attributes);

                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('" + ResourceUtils.getString("text.delete.datafile", item.getModelObject().getFilename()) + "');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }
                    
                    @Override
                    public boolean isVisible() {
                        boolean isOwner = experiment.getPersonByOwnerId().getPersonId() == EEGDataBaseSession.get().getLoggedUser().getPersonId();
                        boolean isAdmin = security.isAdmin();
                        boolean isGroupAdmin = security.userIsAdminInGroup(experiment.getResearchGroup().getResearchGroupId());
                        return isAdmin || isOwner || isGroupAdmin;
                    }

                });

            }
        };

        add(hardware, addParameters, files, software, diseases, pharmaceuticals, projectTypes);

        final WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisibilityAllowed(true);
        
        /* XXX #66 Java Heap Space Exception : working with big data file in memory.
            container.add(experimentViewPanel);
         */
        
        add(container);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

}
