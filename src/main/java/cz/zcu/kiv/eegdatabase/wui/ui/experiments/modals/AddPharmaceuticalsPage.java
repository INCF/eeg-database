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
 *   AddPharmaceuticalsPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import java.util.HashSet;
import java.util.List;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
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

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class AddPharmaceuticalsPage extends WebPage {

    private static final long serialVersionUID = -7751486972344818069L;

    @SpringBean
    PharmaceuticalFacade pharmaceuticalFacade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    public AddPharmaceuticalsPage(final PageReference modalWindowPage,
            final ModalWindow window) {

        add(new AddPharmaceuticalsForm("addForm", window));
    }

    private class AddPharmaceuticalsForm extends Form<Pharmaceutical> {

        private static final long serialVersionUID = 1L;

        public AddPharmaceuticalsForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Pharmaceutical>(new Pharmaceutical()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addPharmaHeader", ResourceUtils.getModel("pageTitle.addPharmaceutical")));

            ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
            List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
            final DropDownChoice<ResearchGroup> researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new Model<ResearchGroup>(), choices, renderer);

            researchGroupChoice.setRequired(true);
            researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
            add(researchGroupChoice);

            TextField<String> title = new TextField<String>("title");
            title.setLabel(ResourceUtils.getModel("label.title"));
            title.add(new TitleExistsValidator());
            title.setRequired(true);
            add(title);

            TextArea<String> description = new TextArea<String>("description");
            description.setLabel(ResourceUtils.getModel("label.pharmaceutical.description"));
            description.setRequired(true);
            add(description);

            AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    Pharmaceutical pharmaceutical = AddPharmaceuticalsForm.this.getModelObject();

                    ResearchGroup group = researchGroupChoice.getModelObject();
                    HashSet<ResearchGroup> groups = new HashSet<ResearchGroup>();
                    groups.add(group);
                    pharmaceutical.setResearchGroups(groups);

                    pharmaceuticalFacade.create(pharmaceutical);
                    window.close(target);
                }
            };
            add(submit);

            add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    window.close(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            }.setDefaultFormProcessing(false));

            setOutputMarkupId(true);
        }
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class TitleExistsValidator implements IValidator<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if (!pharmaceuticalFacade.canSaveTitle(title)) {
                error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
            }
        }
    }
}
