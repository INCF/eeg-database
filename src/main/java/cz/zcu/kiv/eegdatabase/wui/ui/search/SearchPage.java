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
 *   SearchPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * The search page.
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SearchPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    private boolean isSearchStringEmpty = false;

    public SearchPage(PageParameters params) {

        setPageTitle(ResourceUtils.getModel("title.page.search"));

        StringValue searchString = params.get("searchString");
        if (searchString.isEmpty() || searchString.isNull()) {
            isSearchStringEmpty = true;
        }
        ResultCategory category = ResultCategory.getCategory(params.get("category").toString());

        SearchPanel searchPanel = new PageSearchPanel("searchPanel", searchString);
        SearchResultPanel searchResultPanel = new SearchResultPanel("resultPanel", searchString, category) {

            @Override
            public boolean isVisible() {
                return !isSearchStringEmpty;
            }
        };
        add(searchPanel);
        add(searchResultPanel);

        add(new FeedbackPanel("feedback"));
    }
}
