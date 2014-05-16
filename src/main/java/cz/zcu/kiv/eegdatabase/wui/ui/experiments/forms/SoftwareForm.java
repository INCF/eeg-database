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
 *   SoftwareForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import java.util.HashSet;
import java.util.List;

import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import org.apache.wicket.validation.validator.StringValidator;

public class SoftwareForm extends Form<Software> {

    private static final long serialVersionUID = -8012490875088672146L;

    @SpringBean
    private SoftwareFacade facade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    public SoftwareForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Software>(new Software()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addSWHeader", ResourceUtils.getModel("pageTitle.addSoftwareDefinition")));

        ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
        final DropDownChoice<ResearchGroup> researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new Model<ResearchGroup>(), choices, renderer);

        researchGroupChoice.setRequired(true);
        researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
        add(researchGroupChoice);

        TextField<String> title = new TextField<String>("title");
        title.setRequired(true);
        title.setLabel(ResourceUtils.getModel("label.title"));
        add(title);

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.description"));
        description.add(StringValidator.maximumLength(255));
        add(description);

        add(new AjaxButton("submitForm", ResourceUtils.getModel("button.save")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                
                Software newSw = (Software) form.getModelObject();
                ResearchGroup group = researchGroupChoice.getModelObject();

                if (group.getResearchGroupId() == CoreConstants.DEFAULT_ITEM_ID) {
                    if (!facade.canSaveDefaultTitle(newSw.getTitle(), newSw.getSoftwareId())) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        target.add(feedback);
                        return;
                    }
                } else {
                    if (!facade.canSaveTitle(newSw.getTitle(), group.getResearchGroupId(), newSw.getSoftwareId())) {
                        error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                        target.add(feedback);
                        return;
                    }
                }
                HashSet<ResearchGroup> groups = new HashSet<ResearchGroup>();
                groups.add(group);
                newSw.setResearchGroups(groups);

                facade.create(newSw);
                window.close(target);
            }
            
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
            
        });

        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
    }
}
