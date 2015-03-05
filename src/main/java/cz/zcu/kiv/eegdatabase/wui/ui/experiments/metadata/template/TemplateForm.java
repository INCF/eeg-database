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
 *   TemplateForm.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.ByteArrayOutputStream;
import java.util.List;

import odml.core.Section;
import odml.core.Writer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;

public class TemplateForm extends Panel {

    private static final long serialVersionUID = 1L;
    private IModel<Section> model;
    private ModalWindow window;

    @SpringBean
    private TemplateFacade templateFacade;
    private int templateId;

    public TemplateForm(String id) {
        this(id, new Model<Section>(new Section()), 0);
    }

    public TemplateForm(String id, IModel<Section> model, int templateId) {
        super(id);
        this.templateId = templateId;
        this.model = new CompoundPropertyModel<Section>(model);
        setDefaultModel(this.model);
        setOutputMarkupId(true);
        add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(this)));

        final List<Section> sections = templateFacade.getListOfAvailableODMLSections();
        final TemplateTreeViewModel viewModel = new TemplateTreeViewModel();
        Form<Section> form = new Form<Section>("template-form", this.model);
        add(form);

        setupTemplateComponents(sections, viewModel, form);
        setupControlComponents(sections, viewModel, form);
        setupViewTemplateFormComponents();
    }

    private void setupViewTemplateFormComponents() {

        window = new ModalWindow("viewTemplateFormWindow");
        window.setMinimalHeight(500);
        window.setMinimalWidth(800);
        window.setTitle(ResourceUtils.getString("pageTitle.template.view"));
        window.setHeightUnit("px");
        window.setWidthUnit("px");

        ViewSectionPanel panel = new ViewSectionPanel(window.getContentId(), this.model);
        window.setContent(panel);
        add(window);

    }

    private void setupTemplateComponents(final List<Section> sections, final TemplateTreeViewModel viewModel, Form<Section> form) {

        final TemplateSectionPanel templateSectionPanel = new TemplateSectionPanel("template-tree", this.model, TemplateForm.this, sections, viewModel);
        form.add(templateSectionPanel);
    }

    private void setupControlComponents(final List<Section> sections, final TemplateTreeViewModel viewModel, Form<Section> form) {

        AjaxLink<Void> hideDefinition = new AjaxLink<Void>("hideDefinition") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                viewModel.setDefinitionVisible(!viewModel.isDefinitionVisible());
                target.add(TemplateForm.this);
            }
        };

        form.add(hideDefinition);

        AjaxButton saveTemplate = new AjaxButton("save-template", ResourceUtils.getModel("button.template.save")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                Section section = TemplateForm.this.model.getObject();
                if (section.getName() == null || section.getName().isEmpty()) {
                    error(ResourceUtils.getString("text.template.error.templateName"));
                } else {

                    String templateName = section.getName();
                    Writer writer = new Writer(section, true, true);
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

                    if (writer.write(byteStream)) {

                        Template template = new Template();
                        byte[] templateXML = byteStream.toByteArray();

                        if (templateId == 0) {
                            // create new
                            template.setTemplate(templateXML);
                            template.setName(templateName);
                            template.setPersonByPersonId(EEGDataBaseSession.get().getLoggedUser());
                            templateFacade.create(template);
                        } else {
                            // update existing
                            Template updated = templateFacade.read(templateId);
                            updated.setTemplate(templateXML);

                            templateFacade.update(updated);
                        }
                        setResponsePage(ListTemplatePage.class);
                    } else {
                        error(ResourceUtils.getString("text.template.error.save"));
                    }
                }

                target.add(TemplateForm.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(TemplateForm.this);
            }
        };

        form.add(saveTemplate);

        AjaxLink<Void> viewTemplateFormButton = new AjaxLink<Void>("viewTemplateFormButton") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                window.show(target);
            }
        };

        form.add(viewTemplateFormButton);
    }

}
