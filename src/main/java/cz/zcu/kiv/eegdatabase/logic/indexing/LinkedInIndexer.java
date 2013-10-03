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
 *   LinkedInIndexer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.indexing;

import cz.zcu.kiv.eegdatabase.logic.search.ResultCategory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.social.linkedin.api.Post;

import java.io.IOException;

/**
 * LinkedIn indexer.
 * User: Jan Koren
 * Date: 8.3.13
 * Time: 17:36
 */
public class LinkedInIndexer extends Indexer<Post> {

    /**
     * Removes a document that matches the given LinkedIn post (article).
     * @param post The LinkedIn post instance.
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void unindex(Post post) throws IOException, SolrServerException {
        solrServer.deleteById(post.getId());
        solrServer.commit();
    }

    /**
     * Removes all LinkedIn posts (articles) from the Solr index.
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void unindexAll() throws IOException, SolrServerException {
        solrServer.deleteByQuery("source:" + IndexingUtils.SOURCE_LINKEDIN);
        solrServer.commit();
    }

    /**
     * Transforms a LinkedIn post to its document representation.
     * @param post The LinkedIn post.
     * @return Created Lucene/Solr document that represents the LinkedIn post.
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public SolrInputDocument prepareForIndexing(Post post) throws IllegalAccessException, IOException, SolrServerException {
        SolrInputDocument document = new SolrInputDocument();
        document.addField(IndexField.UUID.getValue(), post.getId());
        document.addField(IndexField.ID.getValue(), 1);
        document.addField(IndexField.TITLE.getValue(), post.getTitle());
        document.addField(IndexField.TEXT.getValue(), post.getSummary());
        document.addField(IndexField.CLASS.getValue(), ResultCategory.ARTICLE.getValue());
        document.addField(IndexField.SOURCE.getValue(), IndexingUtils.SOURCE_LINKEDIN);
        return document;
    }
}
