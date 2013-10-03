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
 *   AjaxFeedbackUpdatingBehavior.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 13.5.13
 * Time: 18:06
 */
public class AjaxFeedbackUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
    private FeedbackPanel feedback;

    /**
     * Construct.
     *
     * @param event event to trigger this behavior
     */
    public AjaxFeedbackUpdatingBehavior(String event,
                                        FeedbackPanel feedback) {
        super(event);

        this.feedback = feedback;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        target.add(feedback);
    }

    @Override
    protected void onError(AjaxRequestTarget target, RuntimeException ex){
        super.onError(target,ex);
        target.add(feedback);
    }
}
