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
 *   AjaxDropDownChoice.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form.input;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 * @param <T>
 * @author Jakub Daněk
 */
public class AjaxDropDownChoice<T> extends DropDownChoice<T> {

    public AjaxDropDownChoice(String id) {
        super(id, new Model(), new ArrayList<T>());
    }

    public AjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, new Model(), choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
    }

    public AjaxDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, new Model(), choices);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    public AjaxDropDownChoice(String id, IModel<T> model, List<? extends T> choices) {
        super(id, model, choices);
    }

    public AjaxDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, new Model(), choices, renderer);
    }

    public AjaxDropDownChoice(String id, List<? extends T> choices) {
        super(id, new Model(), choices);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSelectionChangeAjaxified(target, getModelObject());
            }
        });
    }

    protected void onSelectionChangeAjaxified(AjaxRequestTarget target, T option) {
        //override
    }

}
