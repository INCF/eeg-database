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
 *   FormSectionPanel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata;

import odml.core.Property;
import odml.core.Section;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class FormSectionPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public FormSectionPanel(String id, IModel<Section> model) {
        super(id, new CompoundPropertyModel<Section>(model));

        setupLabels();
        setupLists();
    }

    private void setupLists() {

        PropertyListView<Property> properties = new PropertyListView<Property>("properties") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Property> item) {
                item.add(new FormPropertyPanel("property", item.getModel()));
            }
        };
        add(properties.setReuseItems(true));

        PropertyListView<Section> sections = new PropertyListView<Section>("sections") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Section> item) {
                item.add(new FormSectionPanel("section", item.getModel()));
            }
        };
        add(sections.setReuseItems(true));
    }

    private void setupLabels() {
        add(new Label("name"));
        add(new Label("definition"));
    }

}
