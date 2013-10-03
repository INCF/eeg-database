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
 *   AjaxAutoCompletableUpdatingBehavior.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 18.5.13
 * Time: 18:31
 */
public class AjaxAutoCompletableUpdatingBehavior extends AjaxFeedbackUpdatingBehavior{

    private GenericValidator validator;
    private GenericModel model;

    public AjaxAutoCompletableUpdatingBehavior(String event,
                                               FeedbackPanel feedback,
                                               GenericValidator validator,
                                               GenericModel model) {
        super(event, feedback);
        this.validator = validator;
        this.model = model;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        super.onUpdate(target);
        validator.setAutocompleteObject(model.getObject());
    }
}
