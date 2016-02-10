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
 *   SearchResultDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.search;

import cz.zcu.kiv.eegdatabase.logic.search.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.search.FulltextSearchService;
import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.Iterator;

/**
 * Holds search result data.
 * User: Jan Koren
 * Date: 27.3.13
 */
public class SearchResultDataProvider extends ListDataProvider<FullTextResult> {

    @SpringBean
    FulltextSearchService searchService;

    private String searchQuery;
    private ResultCategory category;

    public SearchResultDataProvider(StringValue searchString, ResultCategory category) {
        Injector.get().inject(this);

        System.out.println(searchString);
        if(searchString.isNull()) {
            this.searchQuery = "";
        }
        else {
            this.searchQuery = searchString.toString();
        }

        this.category = category;
    }

    @Override
    public Iterator<FullTextResult> iterator(long first, long count) {
        return searchService.getResultsForQuery(searchQuery, category, (int) first, (int) count).iterator();
    }

    @Override
    public long size() {
        return searchService.getTotalNumberOfDocumentsForQuery(searchQuery, category);
    }

    @Override
    public IModel<FullTextResult> model(final FullTextResult object) {
        return new LoadableDetachableModel<FullTextResult>() {
            @Override
            protected FullTextResult load() {
                return object;
            }
        };
    }
}
