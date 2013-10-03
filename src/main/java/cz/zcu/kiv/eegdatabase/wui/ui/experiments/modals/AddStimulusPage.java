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
 *   AddStimulusPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddStimulusPage extends WebPage {

    @SpringBean
    StimulusFacade stimulusFacade;

    public AddStimulusPage(final PageReference modalWindowPage,
                           final ModalWindow window) {

        AddStimulusForm form = new AddStimulusForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddStimulusForm extends Form<Stimulus> {

        public AddStimulusForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Stimulus>(new Stimulus()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addStimulusHeader", ResourceUtils.getModel("pageTitle.addStimulusDefinition")));

            add(new TextArea<String>("description").add(new DescriptionExistsValidator()).setRequired(true));

            AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Stimulus stimulus = AddStimulusForm.this.getModelObject();
                    validate();
                    target.add(feedback);

                    if (!hasError()) {
                        stimulusFacade.create(stimulus);
                        window.close(target);
                    }
                }
            };
            add(submit);

            add(
                    new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {
                        @Override
                        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            window.close(target);
                        }
                    }.add(new AjaxEventBehavior("onclick") {
                        @Override
                        protected void onEvent(AjaxRequestTarget target) {
                            window.close(target);
                        }
                    })
            );

            setOutputMarkupId(true);
            AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
        }
    }

    private class DescriptionExistsValidator implements IValidator<String>{

        @Override
        public void validate(IValidatable<String> validatable) {
            final String description = validatable.getValue();

            if(!stimulusFacade.canSaveDescription(description)){
                error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
            }
        }
    }
}
