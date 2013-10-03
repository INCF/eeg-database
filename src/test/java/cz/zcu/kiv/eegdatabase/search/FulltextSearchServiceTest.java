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
 *   FulltextSearchServiceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.search;

import cz.zcu.kiv.eegdatabase.logic.search.FullTextResult;
import cz.zcu.kiv.eegdatabase.logic.search.FulltextSearchService;
import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * FullTextSearchService tests.
 *
 * User: Jan Koren
 * Date: 19.3.13
 * Time: 1:15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class FulltextSearchServiceTest {

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private FulltextSearchService fulltextSearchService;

    private Log log = LogFactory.getLog("cz.zcu.kiv.eegdatabase.Tests");

    /**
     * Tests that for a given string, any phrases will be suggested by autocomplete.
     * @throws SolrServerException
     */
    //@Ignore
    @Test
    public void getAutocompleteResultsSuccess() throws SolrServerException {
        String input = "pok";
        Set<String> results = fulltextSearchService.getTextToAutocomplete(input);
        log.info("Autocomplete - found phrases for the string " + input + ":");
        for (String result : results) {
            log.info(result);
        }

        assertTrue(results.size() > 0);
    }

    /**
     * Tests that no results are return by the autocomplete if the input query string
     * does not match any of the stored autocomplete values.
     * @throws SolrServerException
     */
    @Test
    public void getAutocompleteResultsFail() throws SolrServerException {
        String input = "arrrxc";

        Set<String> results = fulltextSearchService.getTextToAutocomplete(input);
        assertTrue(results.size() == 0);
    }

    /**
     * Tests that if the empty query is searched, no results are returned.
     */
    //@Ignore
    @Test
    public void getDocumentCountForEmptyQuery() {
        long count = fulltextSearchService.getTotalNumberOfDocumentsForQuery("", ResultCategory.ALL);
        log.info("Count: " + count);
        assertTrue(count == 0);
    }

    /**
     * It searches everything (by using the * character) and tests that the categories for faceted search are made
     * and that each of them contains at least one document.
     * @throws SolrServerException
     */
    @Test
    public void facetAllTest() throws SolrServerException {

        Map<String, Long> categoryFacets = fulltextSearchService.getCategoryFacets("*");

        for(String category : categoryFacets.keySet()) {
            long count = categoryFacets.get(category);
            log.info(category + " - " + count);
            assertTrue(count > 0);
        }

        assertTrue(categoryFacets.size() == 6);
    }

    /**
     * Tests that having the empty query as the input, no faceting categories will b ecreated.
     */
    @Test
    public void facetNoneTest() {
        Map<String, Long> categoryFacets = fulltextSearchService.getCategoryFacets("");

        for(String category : categoryFacets.keySet()) {
            long count = categoryFacets.get(category);
            log.info(category + " - " + count);
            assertTrue(count == 0);
        }

        assertTrue(categoryFacets.size() == 6);
    }

    /**
     * Tests executing two queries and expects that both will output any search results.
     */
    @Test
    public void searchQuery() {
        List<FullTextResult> results = fulltextSearchService.getResultsForQuery("cha* this", ResultCategory.ALL, 0, 20);
        for(FullTextResult result : results) {
            System.out.println(result.getTitle());
        }
        assertTrue(results.size() > 0);

        results = fulltextSearchService.getResultsForQuery("doma is", ResultCategory.ALL, 0, 20);
        for(FullTextResult result : results) {
            System.out.println(result.getTitle());
        }
        assertTrue(results.size() > 0);
    }
}
