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
 *   AutocompleteIndexer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
