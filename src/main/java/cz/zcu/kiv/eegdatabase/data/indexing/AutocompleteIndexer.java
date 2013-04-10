package cz.zcu.kiv.eegdatabase.data.indexing;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 6.4.13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class AutocompleteIndexer extends Indexer<String> {

    @Override
    public void unindex(String instance) throws IOException, SolrServerException {

    }

    @Override
    public void unindexAll() throws IOException, SolrServerException {

    }

    @Override
    public SolrInputDocument prepareForIndexing(String phrase) throws IllegalAccessException, IOException, SolrServerException {

        SolrInputDocument document = new SolrInputDocument();

        SolrQuery query = new SolrQuery();
        query.setFields(IndexField.AUTOCOMPLETE.getValue());

        query.setQuery("uuid:autocomplete#" + phrase);
        QueryResponse response = solrServer.query(query);

        int count;
        if(response.getResults().size() == 0) {
            count = 0;
        }
        else {
            String foundPhrase = ((List<String>) response.getResults().get(0).getFieldValue(IndexField.AUTOCOMPLETE.getValue())).get(0);
            int delimiterPosition = foundPhrase.lastIndexOf('#');
            if (delimiterPosition == -1) {
                count = 0;
            }
            else {
                count = Integer.valueOf(foundPhrase.substring(foundPhrase.lastIndexOf('#') + 1, foundPhrase.length()));
            }
            count++;
        }

        document.addField(IndexField.UUID.getValue(), "autocomplete#" + phrase);
        document.addField(IndexField.ID.getValue(), 0);
        document.addField(IndexField.AUTOCOMPLETE.getValue(), phrase + "#" + count);

        return document;
    }
}
