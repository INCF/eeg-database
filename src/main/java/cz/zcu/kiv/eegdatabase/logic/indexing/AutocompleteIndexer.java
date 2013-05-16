package cz.zcu.kiv.eegdatabase.logic.indexing;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;

/**
 * The indexer of autocomplete text.
 * User: Jan Koren
 * Date: 6.4.13
 * Time: 20:32
 */
public class AutocompleteIndexer extends Indexer<String> {


    /**
     * @see cz.zcu.kiv.eegdatabase.logic.indexing.Indexer#unindex(Object)
     * @param instance The autocomplete phrase that should be removed from index.
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void unindex(String instance) throws IOException, SolrServerException {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all autocomplete phrases from the Solr index.
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void unindexAll() throws IOException, SolrServerException {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms the searched phrase to a Solr document.
     * @param phrase The input searched phrase.
     * @return The Solr document representing the searched phrase.
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public SolrInputDocument prepareForIndexing(String phrase) throws IllegalAccessException, IOException, SolrServerException {

        SolrInputDocument document = new SolrInputDocument();

        SolrQuery query = new SolrQuery();
        query.setFields(IndexField.AUTOCOMPLETE.getValue());

        query.setQuery("uuid:autocomplete#" + phrase);
        QueryResponse response = solrServer.query(query);

        int count;
        // the phrase is not indexed yet
        if(response.getResults().size() == 0) {
            count = 0;
        }
        // already indexed
        else {
            String foundPhrase = ((List<String>) response.getResults().get(0).getFieldValue(IndexField.AUTOCOMPLETE.getValue())).get(0);
            int delimiterPosition = foundPhrase.lastIndexOf('#');
            // the phrase was originally retrieved by indexing a document
            if (delimiterPosition == -1) {
                count = 0;
            }
            // the phrase was indexed after performing a search query
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
