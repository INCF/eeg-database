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
 *   BaseFeedbackMessagePanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.feedback;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Base feedback panel for global messages.
 * 
 * @author Jakub Rinkes
 * 
 */
public class BaseFeedbackMessagePanel extends FeedbackPanel {

    private static final long serialVersionUID = 2978096327670025839L;

    public BaseFeedbackMessagePanel(String id) {
        this(id, null);
    }

    public BaseFeedbackMessagePanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);

        this.setOutputMarkupId(true);
        this.setOutputMarkupPlaceholderTag(true);
    }

}
