package cz.zcu.kiv.eegdatabase.data.indexing;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.3.13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
public class SocialIndexer<T> extends Indexer<T> {

    @Override
    public void index(T instance) throws Exception {
        SolrInputDocument document = getDocumentFromAnnotatedFields(instance);
        document.setField("source", "linkedIn");

        if(document.isEmpty()) {
            log.info("Nothing added to the solr index.");
            return;
        }

        solrServer.add(document); // if the document already exists, it is replaced by the new one
        UpdateResponse response = solrServer.commit();

    }

    @Override
    public void unindex(T instance) throws Exception {

    }

    public void indexAll() {

    }
}
