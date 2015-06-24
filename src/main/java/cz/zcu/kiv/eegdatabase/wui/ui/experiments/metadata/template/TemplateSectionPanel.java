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
 *   TemplateSectionPanel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.IOException;
import java.util.List;

import odml.core.Property;
import odml.core.Section;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class TemplateSectionPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private IModel<Section> model;
    private WebMarkupContainer content;
    private WebMarkupContainer head;
    private MarkupContainer mainContainer;
    private TemplateTreeViewModel viewModel;
    private int propertySuffix;

    public TemplateSectionPanel(String id, IModel<Section> model, final MarkupContainer container, final List<Section> choices, TemplateTreeViewModel viewModel) {
        super(id, new CompoundPropertyModel<Section>(model));
        this.model = model;
        this.mainContainer = container;
        this.viewModel = viewModel;

        setOutputMarkupId(true);
        content = new WebMarkupContainer("content");
        head = new WebMarkupContainer("head");
        content.setOutputMarkupPlaceholderTag(true);
        add(content, head);
        propertySuffix = model.getObject().propertyCount() + 1;

        setupHeadAndControlComponents(model, choices);
        setupContentComponents(choices);
    }

    private void setupContentComponents(final List<Section> choices) {

        PropertyListView<Property> properties = new PropertyListView<Property>("properties") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Property> item) {
                item.add(new TemplatePropertyPanel("property", item.getModel(), viewModel, content));
            }
        };
        content.add(properties);

        PropertyListView<Section> sections = new PropertyListView<Section>("subsections") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Section> item) {
                item.add(new TemplateSectionPanel("section", item.getModel(), mainContainer, choices, viewModel));
            }
        };
        content.add(sections);
    }

    private void setupHeadAndControlComponents(final IModel<Section> model, final List<Section> choices) {
        // head + control
        final boolean notRootSection = model.getObject().getParent() != null;

        head.add(new AjaxEditableLabel<String>("name") {

            private static final long serialVersionUID = 1L;

            @Override
            protected String defaultNullLabel() {

                if (notRootSection) {
                    return ResourceUtils.getString("text.template.empty.sectionName");
                } else {
                    return ResourceUtils.getString("text.template.empty.templateName");
                }
            }
        });

        String key;
        if (notRootSection) {
            key = "label.template.section";
        } else {
            key = "label.template.templateName";
        }

        head.add(new Label("sectionNameLabel", ResourceUtils.getString(key)));

        head.add(new AjaxEditableMultiLineLabel<String>("definition") {

            private static final long serialVersionUID = 1L;

            @Override
            protected String defaultNullLabel() {
                return ResourceUtils.getString("text.template.empty.section.definition");
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(viewModel.isDefinitionVisible());
            }
        }.setVisibilityAllowed(notRootSection));

        final DropDownChoice<Section> sectionList = new DropDownChoice<Section>("addSubsectionList", new Model<Section>(), choices, new ChoiceRenderer<Section>("name"));
        sectionList.setOutputMarkupId(true);
        head.add(sectionList);
        sectionList.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Section section = sectionList.getModelObject();
                try {
                    section = section.copy();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TemplateSectionPanel.this.model.getObject().add(section);
                sectionList.setModelObject(null);
                target.add(mainContainer);
            }

        });

        head.add(new AjaxLink<Void>("removeSection") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                Section section = TemplateSectionPanel.this.model.getObject();

                section.getParent().removeSection(section);
                target.add(mainContainer);
            }

        }.setVisibilityAllowed(notRootSection));

        head.add(new AjaxLink<Void>("showhideSectionContent") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                content.setVisible(!content.isVisible());
                target.add(content);
            }

        });

        head.add(new AjaxLink<Void>("addProperty") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    Property property = new Property(ResourceUtils.getString("text.template.empty.propertyName") + propertySuffix++, ResourceUtils.getString("text.template.empty.propertyValue"));
                    TemplateSectionPanel.this.model.getObject().add(property);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                target.add(mainContainer);
            }

        }.setVisibilityAllowed(notRootSection));
    }

}
