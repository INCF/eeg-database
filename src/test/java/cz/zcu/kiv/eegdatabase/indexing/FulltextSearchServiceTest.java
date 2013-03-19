package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.logic.controller.searchsolr.FulltextSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

import java.util.Set;

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
}
