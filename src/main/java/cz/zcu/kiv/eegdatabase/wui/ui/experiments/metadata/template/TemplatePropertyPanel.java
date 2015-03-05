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
 *   TemplatePropertyPanel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import odml.core.Property;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class TemplatePropertyPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private TemplateTreeViewModel viewModel;
    private MarkupContainer container;

    public TemplatePropertyPanel(String id, IModel<Property> model, TemplateTreeViewModel viewModel, MarkupContainer container) {
        super(id, new CompoundPropertyModel<Property>(model));
        this.viewModel = viewModel;
        this.container = container;
        this.setOutputMarkupId(true);

        setupComponents(model);
    }

    private void setupComponents(IModel<Property> model) {

        add(new AjaxEditableLabel<String>("name"){
            
            private static final long serialVersionUID = 1L;

            @Override
            protected String defaultNullLabel() {
                return ResourceUtils.getString("text.template.empty.propertyName");
            }
            
        });
        add(new AjaxEditableLabel<Object>("value"){
            
            private static final long serialVersionUID = 1L;
            
            @Override
            protected String defaultNullLabel() {
                return ResourceUtils.getString("text.template.empty.propertyValue");
            }
            
        });
        add(new AjaxEditableMultiLineLabel<String>("definition") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(TemplatePropertyPanel.this.viewModel.isDefinitionVisible());
            }
            
            @Override
            protected String defaultNullLabel() {
                return ResourceUtils.getString("text.template.empty.definition");
            }
        });
        
        add(new AjaxEditableLabel<String>("type"){
            
            private static final long serialVersionUID = 1L;

            @Override
            protected String defaultNullLabel() {
                return ResourceUtils.getString("text.template.empty.propertyType");
            }
        });
        
        add(new AjaxLink<Void>("remove-property"){

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                Property property = (Property) TemplatePropertyPanel.this.getDefaultModelObject();
                property.getParent().removeProperty(property.getName());
                
                target.add(container);
            }
            
            
        });
    }

}
