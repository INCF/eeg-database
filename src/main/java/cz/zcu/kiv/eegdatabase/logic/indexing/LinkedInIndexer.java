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
