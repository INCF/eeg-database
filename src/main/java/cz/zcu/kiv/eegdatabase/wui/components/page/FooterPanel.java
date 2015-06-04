/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * FooterPanel.java, 2. 6. 2015 11:26:48, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.page;

import java.util.Calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

/**
 * @author Jakub Krauz
 *
 */
public class FooterPanel extends Panel {
    
    private static final long serialVersionUID = -1105454027657402307L;

    public FooterPanel(String id) {
        super(id);
        
        add(new ExternalLink("footerLink", ResourceUtils.getString("general.footer.link"), ResourceUtils.getString("general.footer.link.title")));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        add(new Label("year", ResourceUtils.getString("general.footer.year") + year));
    }

}
