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
 *   RepeatableInput.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:28
 */
public class RepeatableInput<T extends IAutoCompletable> extends AutoCompleteTextField<T> {
    private final int AUTO_COMPLETE_CHOICES = 10;
    protected GenericFacade<T, Integer> service;

    public RepeatableInput(String id,
                           IModel<T> model,
                           Class<T> type,
                           GenericFacade<T, Integer> service) {
        super(id, model, type, new AbstractAutoCompleteTextRenderer<T>() {
            @Override
            protected String getTextValue(T object) {
                return object.getAutoCompleteData();
            }
        }, new AutoCompleteSettings());
        this.service = service;

        setOutputMarkupId(true);
    }

    @Override
    protected Iterator<T> getChoices(String input) {
        if(Strings.isEmpty(input)){
            return new ArrayList<T>().iterator();
        }
        List<T> allChoices = service.getAllRecords();
        List<T> choices = new ArrayList<T>(AUTO_COMPLETE_CHOICES);
        for(T t: allChoices) {
            if((t.getAutoCompleteData() != null) &&
                    t.getAutoCompleteData().toLowerCase().startsWith(input.toLowerCase())){
                choices.add(t);
            }
            if(choices.size() >= AUTO_COMPLETE_CHOICES) {
                break;
            }
        }
        return choices.iterator();
    }
}
