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
 *   ViewLinkPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;

import java.util.List;

/**
 * Simple Panel with link, this link redirect on pages in constructor. And Label for this links is definied by property expression.
 * 
 * Model - contains data object.
 * PropertyExpression - tell model what information is used like parameter for redirect and label of links if displayProperty addded. 
 * Page is page where will be redirect after click on link.
 * DisplayExpression - tell model what information is used for label if its different like propertyExpression
 * 
 * @author Jakub Rinkes
 * 
 */
public class ViewLinkPanel extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model) {
        super(id);
        PropertyModel viewModel = new PropertyModel(model, propertyExpression);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(viewModel.getObject()))
                .add(new Label("label", viewModel)));
    }

    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model, String displayProperty) {
        super(id);
        PropertyModel paramModel = new PropertyModel(model, propertyExpression);
        PropertyModel viewModel = new PropertyModel(model, displayProperty);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(paramModel.getObject()))
                .add(new Label("label", viewModel)));
    }

    public ViewLinkPanel(String id, Class<? extends MenuPage> page, String propertyExpression, IModel model, IModel<String> displayModel) {
        super(id);
        PropertyModel param = new PropertyModel(model, propertyExpression);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getDefaultPageParameters(param.getObject()))
                .add(new Label("label", displayModel)));
    }

    public ViewLinkPanel(String id, Class<? extends MenuPage> page, List<String> parameterNames, List<Object> parameterValues, IModel<String> displayModel) {
        super(id);
        add(new BookmarkablePageLink("link", page, PageParametersUtils.getPageParameters(parameterNames, parameterValues))
                .add(new Label("label", displayModel)));
    }
}
