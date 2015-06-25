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
 *   PropertyMultiValuePanel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.Serializable;

import odml.core.Property;
import odml.core.Value;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

public class PropertyMultiValuePanel extends Panel {

    private static final long serialVersionUID = 1L;

    private int valueSuffix = 1;
    private Property property;

    public PropertyMultiValuePanel(String id, final IModel<Property> model) {
        super(id, new CompoundPropertyModel<Property>(model));
        setOutputMarkupId(true);
        property = model.getObject();
        valueSuffix = property.valueCount() + 1;

        PropertyListView<Value> values = new PropertyListView<Value>("values") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Value> item) {

                int index = item.getIndex();
                item.add(new AjaxEditableLabel<Serializable>("content", new PropertyValueModel(property, index)) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected String defaultNullLabel() {
                        return "...";
                    }
                });
                item.add(new AjaxLink<Void>("removeValueLink") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        int index = item.getIndex();
                        property.removeValue(index);
                        target.add(PropertyMultiValuePanel.this);
                    }

                    @Override
                    protected void onConfigure() {
                        super.onConfigure();
                        setVisible(property.valueCount() > 1);
                    }
                });
            }
        };
        values.setReuseItems(true);

        add(values);

        AjaxLink<Void> addLink = new AjaxLink<Void>("addButton") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                property.addValue(ResourceUtils.getString("text.template.empty.propertyValue") + valueSuffix++);
                target.add(PropertyMultiValuePanel.this);
            }
        };

        add(addLink);
    }

}
