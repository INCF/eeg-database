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
 *   AddExperimentResultsForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard;

import java.util.List;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;

public class AddExperimentResultsForm extends WizardStep {

    private static final long serialVersionUID = 8769628527223589880L;

    private FileUploadField fileUploadField;

    @SpringBean
    private ExperimentsFacade experimentsFacade;

    private IModel<List<FileUpload>> model;

    public AddExperimentResultsForm(IModel<List<FileUpload>> model){
        
        this.model = model;
        addFileUploadField();
    }

    private void addFileUploadField(){
        
        fileUploadField = new FileUploadField("resultFile", model);

        ComponentFeedbackMessageFilter fileFilter = new ComponentFeedbackMessageFilter(fileUploadField);
        final FeedbackPanel fileFeedback = new FeedbackPanel("fileFeedback", fileFilter);
        fileFeedback.setOutputMarkupId(true);

        add(fileUploadField);
        add(fileFeedback);
    }

}
