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
 *   GroupForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.resource.ResourceUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 13:53
 */
public class GroupForm extends Form<ResearchGroup> {
    @SpringBean
    ResearchGroupFacade researchGroupFacade;


    public GroupForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<ResearchGroup>(new ResearchGroup()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        add(new Label("addGroupHeader", ResourceUtils.getModel("pageTitle.addGroupDefinition")));

        RequiredTextField<String> title = new RequiredTextField<String>("title");
        title.setLabel(ResourceUtils.getModel("label.title"));
        title.add(new TitleExistsValidator());
        add(title);

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel((ResourceUtils.getModel("label.description")));
        add(description);

        add(
                new AjaxButton("submitForm", this) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        ResearchGroup researchGroup = (ResearchGroup) form.getModelObject();

                        validate();
                        target.add(feedback);

                        if(!hasError()){
                            researchGroup.setPerson(EEGDataBaseSession.get().getLoggedUser());
                            researchGroupFacade.create(researchGroup);
                            window.close(target);
                        }
                    }
                }.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                    }
                })
        );

        add(
                new AjaxButton("closeForm", this) {}.
                        add(new AjaxEventBehavior("onclick") {
                            @Override
                            protected void onEvent(AjaxRequestTarget target) {
                                window.close(target);
                            }
                        })
        );

        setOutputMarkupId(true);
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
    }

    private class TitleExistsValidator implements IValidator<String> {

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if(researchGroupFacade.existsGroup(title)){
                error("Research group with given name already exists.");
            }
        }
    }
}
