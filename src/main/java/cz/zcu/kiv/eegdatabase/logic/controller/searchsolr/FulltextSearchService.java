package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 12.3.13
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class FulltextSearchService {

    @Autowired
    private SolrServer solrServer;

    public SolrDocumentList getAllDocuments() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery(":*:");
        QueryResponse response = solrServer.query(query);
        return response.getResults();
    }
}
