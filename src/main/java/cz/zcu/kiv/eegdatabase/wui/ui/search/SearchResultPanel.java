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
 *   SearchResultPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.indexing.IndexingUtils;
import cz.zcu.kiv.eegdatabase.logic.search.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.StringValue;

import java.io.Serializable;

/**
 * This component is responsible for displaying full text search results.
 *
 * User: Jan Koren
 * Date: 26.3.13
 * Time: 1:13
 */
public class SearchResultPanel extends Panel {

    private static long serialVersionUID = 1L;
    private static final int ITEMS_PER_PAGE = 20;

    private boolean isModelEmpty = false;
    private boolean foundResultsPerMultiplePages = false;

    public SearchResultPanel(String id, StringValue searchString, ResultCategory category) {
        super(id);
        IDataProvider<FullTextResult> fullTextResults = new SearchResultDataProvider(searchString, category);
        long foundResults = fullTextResults.size();
        if(foundResults == 0) {
            isModelEmpty = true;
        }
        if(foundResults > ITEMS_PER_PAGE) {
            foundResultsPerMultiplePages = true;
        }

        add(new Label("numberOfResults", ResourceUtils.getString("text.search.numberOfResultsFound",
                new Model<Serializable>(foundResults))) {
            @Override
            public boolean isVisible() {
                return !isModelEmpty; // visible if any documents are found
            }
        });

        add(new Label("noResultsFound", ResourceUtils.getString("text.search.noResults")) {
            @Override
            public boolean isVisible() {
                return isModelEmpty; // visible if no documents are found
            }
        });

        add(new SearchFacets("categories", searchString, category));

        SearchResultList searchResultList =  new SearchResultList("results", fullTextResults, ITEMS_PER_PAGE);
        add(searchResultList);
        add(new PagingNavigator("navigator", searchResultList) {
            @Override
            public boolean isVisible() {
                return foundResultsPerMultiplePages;
            }
        });
    }

    /**
     * List that renders full-text results.
     */
    private class SearchResultList extends DataView<FullTextResult> {

        protected SearchResultList(String id, IDataProvider<FullTextResult> dataProvider, long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
        }

        @Override
        public void populateItem(Item<FullTextResult> item) {
            item.setModel(new CompoundPropertyModel<FullTextResult>(item.getModel()));

            String title = item.getModelObject().getTitle();
            if(title.isEmpty()) {
                title = ResourceUtils.getString("text.search.results.title.empty");
            }

            final String source = item.getModelObject().getSource();
            String sourceString = "";
            if(source.equals(IndexingUtils.SOURCE_LINKEDIN)) {
                sourceString = ResourceUtils.getString("text.search.source.linkedin");
            }
            else {
                sourceString = ResourceUtils.getString("text.search.source.database");
            }

            item.add(new Label("title", title).setEscapeModelStrings(false));
            item.add(new Label("type"));
            item.add(new Label("source", sourceString) {
                @Override
                public boolean isVisible() {
                    return source.equals(IndexingUtils.SOURCE_LINKEDIN);
                }
            });
            item.add(new ListView<String>("textFragments") {
                @Override
                protected void populateItem(ListItem<String> item) {
                    item.add(new Label("textFragment", item.getModelObject()).setEscapeModelStrings(false));
                }
            });
            item.add(new DateLabel("timestamp", new PatternDateConverter(StringUtils.DATE_TIME_FORMAT_PATTER, true)));
            BookmarkablePageLink link = new BookmarkablePageLink<Void>("link", item.getModelObject().getTargetPage(),
                    PageParametersUtils.getDefaultPageParameters(item.getModelObject().getId()));
            item.add(link);
        }
    }
}
