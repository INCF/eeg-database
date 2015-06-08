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
 * LabeledLink.java, 8. 6. 2015 13:06:36, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.link;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;

/**
 * @author Jakub Krauz
 *
 */
public abstract class LabeledLink extends Link<Void> {

    private static final long serialVersionUID = -9182016972650231640L;
    
    
    public LabeledLink(String id, IModel<String> label) {
        super(id);
        add(new Label("label", label).setRenderBodyOnly(true));
    }

    
    public LabeledLink(String id, String label) {
        super(id);
        add(new Label("label", label).setRenderBodyOnly(true));
    }
    
    
    @Override
    protected final IMarkupSourcingStrategy newMarkupSourcingStrategy() {
        return new PanelMarkupSourcingStrategy(true);
    }

}
