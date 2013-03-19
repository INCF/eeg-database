package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 12.3.13
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FulltextSearchService {

    @Autowired
    private SolrServer solrServer;

    public SolrDocumentList getAllDocuments() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*");
        QueryResponse response = solrServer.query(query);
        return response.getResults();
    }

    /**
     * Gets values to autocomplete for the input string.
     * The autocomplete feature works for multivalued fields and is based on a highlighting trick.
     * See http://solr.pl/en/2013/02/25/autocomplete-on-multivalued-fields-using-highlighting/
     * @param keywordStart
     * @return
     * @throws SolrServerException
     */
    public Set<String> getTextToAutocomplete(String keywordStart) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery(IndexField.AUTOCOMPLETE.getValue()+":"+keywordStart);
        query.setFields(IndexField.AUTOCOMPLETE.getValue());
        query.setHighlight(true);
        query.setParam("hl.fl", IndexField.AUTOCOMPLETE.getValue());
        query.setHighlightSimplePre("");
        query.setHighlightSimplePost("");
        query.setRows(10);

        Set<String> autocompleteList = new HashSet<String>();

        QueryResponse response = solrServer.query(query);
        Set<String> foundIds = response.getHighlighting().keySet();
        for(String id : foundIds) {
            List<String> resultsPerDocument = response.getHighlighting().get(id).get(IndexField.AUTOCOMPLETE.getValue());
            for (String result : resultsPerDocument) {
                autocompleteList.add(result.toLowerCase());
            }

            if(autocompleteList.size() == 10) {
                break;
            }
        }

        return autocompleteList;
    }
}
