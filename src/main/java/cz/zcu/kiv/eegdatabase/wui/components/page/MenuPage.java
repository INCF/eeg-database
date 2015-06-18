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
 *   MenuPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.page;

import org.apache.wicket.markup.head.IHeaderResponse;

import cz.zcu.kiv.eegdatabase.wui.components.feedback.BaseFeedbackMessagePanel;
import de.agilecoders.wicket.core.Bootstrap;

/**
 * MenuPage for EEGDatabase portal, added header section with information about logged user,
 * added base feedback panel and footer link.
 * 
 * @author Jakub Rinkes
 * @author Jakub Krauz
 *
 */
public class MenuPage extends BasePage {

    private static final long serialVersionUID = -9208758802775141621L;

    private BaseFeedbackMessagePanel feedback;

    public MenuPage() {
        
        // header
        add(new HeaderPanel("header"));

        feedback = new BaseFeedbackMessagePanel("base_feedback");
        add(feedback);

        // footer
        add(new FooterPanel("footer"));
    }

    public BaseFeedbackMessagePanel getFeedback() {
        return feedback;
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        //response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
        Bootstrap.renderHead(response);
    }
}
