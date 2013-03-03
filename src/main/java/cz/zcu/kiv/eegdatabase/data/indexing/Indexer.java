package cz.zcu.kiv.eegdatabase.data.indexing;

import org.apache.solr.client.solrj.SolrServer;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 28.2.13
 * Time: 1:50
 * To change this template use File | Settings | File Templates.
 */
public interface Indexer<T> {

    void index(T instance) throws Exception; // TODO specify (own) exceptions

    void unindex(T instance) throws Exception; // TODO specify (own) exceptions
}
