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
 *   ViewMetadataPropertyPanel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata;

import odml.core.Property;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template.PropertyValueModel;

public class ViewMetadataPropertyPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public ViewMetadataPropertyPanel(String id, IModel<Property> model) {
        super(id, new CompoundPropertyModel<Property>(model));

        Label name = new Label("name");
        Label value = new Label("value", new PropertyValueModel(model.getObject()));

        String definition = model.getObject().getDefinition();
        if (definition != null && !definition.isEmpty()) {
            String tooltip = ResourceUtils.getString("label.template.definition.tooltip") + definition;
            name.add(AttributeModifier.append("title", tooltip));
            value.add(AttributeModifier.append("title", tooltip));
        }
        
        add(name, value);
    }

}
