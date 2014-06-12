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
 *   AddDataFilePage.java, 2014/06/12 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ListExperimentsPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class AddDataFilePage extends MenuPage {

    private static final long serialVersionUID = -1065232841030454856L;
    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private ExperimentsFacade facade;

    @SpringBean
    private FileFacade fileFacade;

    private MultiFileUploadField fileUploadField;
    private IModel<List<FileUpload>> model;

    public AddDataFilePage(PageParameters parameters) {

        StringValue stringValue = parameters.get(DEFAULT_PARAM_ID);
        if (stringValue.isNull() || stringValue.isEmpty()) {
            throw new RestartResponseAtInterceptPageException(ListExperimentsPage.class);
        }

        int experimentId = stringValue.toInt();

        setPageTitle(ResourceUtils.getModel("pageTitle.addDataFile"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        setupComponents(experimentId);
    }

    private void setupComponents(final int experimentId) {

        final Experiment experiment = facade.read(experimentId);
        model = new ListModel<FileUpload>(new ArrayList<FileUpload>());
        fileUploadField = new MultiFileUploadField("file", model);

        Form<List<FileUpload>> uploadForm = new Form<List<FileUpload>>("uploadForm");

        ComponentFeedbackMessageFilter fileFilter = new ComponentFeedbackMessageFilter(fileUploadField);
        final FeedbackPanel fileFeedback = new FeedbackPanel("fileFeedback", fileFilter);
        fileFeedback.setOutputMarkupId(true);

        uploadForm.add(fileUploadField);
        uploadForm.add(fileFeedback);

        AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.addDataFile")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(fileFeedback);
                target.add(getFeedback());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                try {
                    
                    List<FileUpload> fileUploadList = model.getObject();
                    if (!fileUploadList.isEmpty()) {
                        for (FileUpload fileUpload : fileUploadList) {

                            DataFile file = new DataFile();
                            file.setMimetype(fileUpload.getContentType());
                            file.setFilename(fileUpload.getClientFileName());
                            file.setFileContentStream(fileUpload.getInputStream());
                            file.setExperiment(experiment);
                            fileFacade.create(file);
                        }
                    }

                    setResponsePage(ExperimentsDetailPage.class,
                            PageParametersUtils.getDefaultPageParameters(experimentId));
                }
                catch (Exception ex) {
                    error(ResourceUtils.getString("error.file.add.error"));
                    log.error(ex.getMessage(), ex);
                    target.add(getFeedback());
                    target.add(fileFeedback);
                }
            }
        };
        uploadForm.add(submit);
        add(uploadForm);
    }

}
