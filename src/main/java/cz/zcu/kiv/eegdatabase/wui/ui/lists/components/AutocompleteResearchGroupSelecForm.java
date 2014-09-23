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
 *   AutocompleteResearchGroupSelecForm.java, 2014/23/09 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.Strings;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public class AutocompleteResearchGroupSelecForm extends Form<Void> {

    private static final long serialVersionUID = -7114935891544773069L;

    protected Log log = LogFactory.getLog(getClass());

    private ListModel<ResearchGroup> choices;

    public AutocompleteResearchGroupSelecForm(String id, final IModel<ResearchGroup> model, ListModel<ResearchGroup> choices, final MarkupContainer container) {
        super(id);
        this.setOutputMarkupPlaceholderTag(true);

        this.choices = choices;

        AutoCompleteSettings settings = prepareAutoCompleteSettings();

        AbstractAutoCompleteTextRenderer<ResearchGroup> renderer = new AbstractAutoCompleteTextRenderer<ResearchGroup>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getTextValue(ResearchGroup object) {
                return object.getTitle();
            }
        };

        final AutoCompleteTextField<ResearchGroup> groupField = new AutoCompleteTextField<ResearchGroup>("groupField", new Model<ResearchGroup>(),
                ResearchGroup.class, renderer, settings) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<ResearchGroup> getChoices(String input) {

                List<ResearchGroup> choices;
                List<ResearchGroup> allChoices = AutocompleteResearchGroupSelecForm.this.choices.getObject();

                if (Strings.isEmpty(input)) {
                    choices = allChoices;
                } else {

                    choices = new ArrayList<ResearchGroup>(10);
                    for (ResearchGroup t : allChoices) {
                        if ((t.getTitle() != null) &&
                                t.getTitle().toLowerCase().contains(input.toLowerCase())
                                || t.getTitle().toLowerCase().startsWith(input.toLowerCase())) {
                            choices.add(t);
                        }
                    }
                }
                Collections.sort(choices);
                return choices.iterator();
            }
        };

        groupField.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                ResearchGroup group = groupField.getModelObject();
                model.setObject(group);

                target.add(container);
            }
        });

        add(groupField);
    }

    private AutoCompleteSettings prepareAutoCompleteSettings() {

        AutoCompleteSettings settings = new AutoCompleteSettings();
        settings.setShowListOnEmptyInput(true);
        settings.setShowCompleteListOnFocusGain(true);
        settings.setUseHideShowCoveredIEFix(false);
        settings.setMaxHeightInPx(200);
        settings.setAdjustInputWidth(false);
        return settings;
    }

}
