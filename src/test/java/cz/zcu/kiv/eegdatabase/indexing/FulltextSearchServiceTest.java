package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;
import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.3.13
 * Time: 1:15
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class FulltextSearchServiceTest {

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private FulltextSearchService fulltextSearchService;

    private Log log = LogFactory.getLog("cz.zcu.kiv.eegdatabase.Tests");

    @Ignore
    @Test
    public void getResults() throws SolrServerException {
        Set<String> results = fulltextSearchService.getTextToAutocomplete("pok");
        log.info("Autocomplete - found phrases:");
        for (String result : results) {
            log.info(result);
        }

        assertTrue(results.size() > 0);

        // finds nothing
        results = fulltextSearchService.getTextToAutocomplete("arrrxc");
        assertTrue(results.size() == 0);
    }

    @Ignore
    @Test
    public void getDocumentCountForTestQuery() {
        long count = fulltextSearchService.getTotalNumberOfDocumentsForQuery("");
        log.info("Count: " + count);
        assertTrue(count == 0);
    }

    @Test
    public void facetTest() throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery("linkedin");
        solrQuery.setParam("df",IndexField.SOURCE.getValue());
        solrQuery.setFacet(true);
        solrQuery.addFacetField(IndexField.SOURCE.getValue());

        QueryResponse response = solrServer.query(solrQuery);
        for(FacetField field : response.getFacetFields()) {
            System.out.println("count: " + field.getValueCount());
            for (Count count : field.getValues()) {
                System.out.println(count.getName() + ", " + count.getCount());
            }
        }
    }
}
