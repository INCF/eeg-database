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
 *   ExperimentFormPage.java, 2013/10/17 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeConf;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.SubjectGroup;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.AjaxWizardButtonBar;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard.AddExperimentEnvironmentForm;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard.AddExperimentResultsForm;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard.AddExperimentScenarioForm;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;
import org.hibernate.Hibernate;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentFormPage extends MenuPage {

    private static final long serialVersionUID = 5729268495591680609L;

    private Log log = LogFactory.getLog(getClass());

    @SpringBean
    private ExperimentsFacade facade;
    
    @SpringBean
    private FileFacade fileFacade;

    public ExperimentFormPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        final Model<Experiment> model = new Model<Experiment>(new Experiment());
        setupComponents(model);
    }

    public ExperimentFormPage(PageParameters parameters) {

        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        int experimentId = parseParameters(parameters);
        final Model<Experiment> model = new Model<Experiment>(facade.getExperimentForDetail(experimentId));
        setupComponents(model);
    }

    private void setupComponents(final Model<Experiment> model) {
        final ListModel<FileUpload> fileModel = new ListModel<FileUpload>(new ArrayList<FileUpload>());

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(this);
        getFeedback().setFilter(filter);

        WizardModel wizardModel = new WizardModel();
        wizardModel.add(new AddExperimentScenarioForm(model));
        wizardModel.add(new AddExperimentEnvironmentForm(model));
        wizardModel.add(new AddExperimentResultsForm(fileModel));

        Wizard wizard = new Wizard("wizard", wizardModel, false) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onFinish() {

                Experiment experiment = model.getObject();

                Set<DataFile> files = new HashSet<DataFile>();
                try {
                    List<FileUpload> fileUploadList = fileModel.getObject();
                    if (!fileUploadList.isEmpty()) {
                        for (FileUpload fileUpload : fileUploadList) {
                            DataFile file = new DataFile();

                            byte[] fileBytes = fileUpload.getBytes();
                            file.setFileContent(Hibernate.createBlob(fileBytes));
                            file.setMimetype(fileUpload.getContentType());
                            file.setFilename(fileUpload.getClientFileName());
                            files.add(file);
                        }
                    }
                }
                catch (Exception ex) {
                    error("File saving failed");
                }

                // TODO user must somehow insert this data instead of us.
                Person logged = EEGDataBaseSession.get().getLoggedUser();
                experiment.setPersonByOwnerId(logged);
                Digitization digitization = new Digitization();
                digitization.setDigitizationId(52);
                experiment.setDigitization(digitization);
                SubjectGroup subjectGroup = new SubjectGroup();
                subjectGroup.setSubjectGroupId(11);
                experiment.setSubjectGroup(subjectGroup);
                Artifact artifact = new Artifact();
                artifact.setArtifactId(5);
                experiment.setArtifact(artifact);
                ElectrodeConf conf = new ElectrodeConf();
                conf.setElectrodeConfId(6);
                experiment.setElectrodeConf(conf);
                // TODO End of todo.

                Integer id = experiment.getExperimentId();
                if (experiment.getExperimentId() != 0)
                    facade.update(experiment);
                else
                    id = facade.create(experiment);
                
                Experiment read = facade.read(id);
                
                for(DataFile file :files){
                    file.setExperiment(experiment);
                    fileFacade.create(file);
                }

                setResponsePage(ExperimentsDetailPage.class, PageParametersUtils.getDefaultPageParameters(id));
            }

            @Override
            protected Component newFeedbackPanel(String id) {

                ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(this);
                return new FeedbackPanel(id, filter);
            }
            
            @Override
            protected Component newButtonBar(String id) {
                return new AjaxWizardButtonBar(id, this);
            }
            
            @Override
            public void onCancel() {
                throw new RestartResponseAtInterceptPageException(ExperimentsDetailPage.class, getPageParameters());
            }
            
        };

        add(wizard);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

}
