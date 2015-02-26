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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import odml.core.Reader;
import odml.core.Section;
import odml.core.Writer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;

public class TemplateForm extends Panel {

    private static final long serialVersionUID = 1L;
    private IModel<Section> model;
    private ModalWindow window;

    public TemplateForm(String id) {
        this(id, new Model<Section>(new Section()));
    }

    public TemplateForm(String id, IModel<Section> model) {
        super(id);
        this.model = new CompoundPropertyModel<Section>(model);
        setDefaultModel(this.model);
        setOutputMarkupId(true);
        add(new FeedbackPanel("feedback"));

        // TODO change this with template facade
        Set<String> files = EEGDataBaseApplication.get().getServletContext().getResourcePaths("/files/odML/odMLSections");
        List<String> sectionPaths = new ArrayList<String>();
        for (String path : files) {
            if (path.endsWith(".xml"))
                sectionPaths.add(path);
        }
        final List<Section> sections = new ArrayList<Section>();
        Reader reader = new Reader();
        for (String path : sectionPaths) {
            InputStream file = EEGDataBaseApplication.get().getServletContext().getResourceAsStream(path);
            Section section = null;
            try {
                section = reader.load(file);
                sections.addAll(section.getSections());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // end TODO

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
        window.setTitle("pageTitle.template.view");
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

        AjaxButton saveTemplate = new AjaxButton("save-template", new Model<String>("button.template.save")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                try {
                    FileOutputStream stream = new FileOutputStream(new File("D:\\trash\\savetemplate.xml"));
                    
                    Section rootSection = new Section();
                    rootSection.add(TemplateForm.this.model.getObject());
                    Writer writer = new Writer(rootSection, true, false);
                    if (writer.write(stream)) {
                        info("Save template done.");
                    } else {
                        error("Save template failed.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
